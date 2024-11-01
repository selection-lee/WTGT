import argparse
import time
from pathlib import Path
import cv2
import torch
import torch.backends.cudnn as cudnn
import requests
import numpy as np
from numpy import random
import os
import easyocr

from models.experimental import attempt_load
from utils.datasets import LoadStreams, LoadImages
from utils.general import check_img_size, increment_path, non_max_suppression, scale_coords
from utils.plots import plot_one_box
from utils.torch_utils import select_device, time_synchronized

# OpenMP 오류 방지 설정
torch.set_num_threads(1)
os.environ["OMP_NUM_THREADS"] = "1"

# EasyOCR 초기화 - GPU 사용
reader = easyocr.Reader(['ko', 'en'], gpu=True)

def detect_and_ocr(save_img=False):
    try:
        source, weights, view_img, save_txt, imgsz, trace = opt.source, opt.weights, opt.view_img, opt.save_txt, opt.img_size, not opt.no_trace
        save_img = not opt.nosave and not source.endswith('.txt')
        webcam = source.isnumeric() or source.endswith('.txt') or source.lower().startswith(
            ('rtsp://', 'rtmp://', 'http://', 'https://'))

        # Directories
        save_dir = Path(increment_path(Path(opt.project) / opt.name, exist_ok=opt.exist_ok))
        (save_dir / 'labels' if save_txt else save_dir).mkdir(parents=True, exist_ok=True)

        # Initialize
        device = select_device(opt.device)
        half = device.type != 'cpu'

        # Load model
        model = attempt_load(weights, map_location=device)
        stride = int(model.stride.max())
        imgsz = check_img_size(imgsz, s=stride)

        if half:
            model.half()

        # Set Dataloader
        if webcam:
            cudnn.benchmark = True
            dataset = LoadStreams(source, img_size=imgsz, stride=stride)
        else:
            dataset = LoadImages(source, img_size=imgsz, stride=stride)

        # Get names and colors
        names = model.module.names if hasattr(model, 'module') else model.names
        colors = [[random.randint(0, 255) for _ in range(3)] for _ in names]

        # Run inference
        t0 = time.time()
        for path, img, im0s, vid_cap in dataset:
            img = torch.from_numpy(img).to(device)
            img = img.half() if half else img.float()
            img /= 255.0
            if img.ndimension() == 3:
                img = img.unsqueeze(0)

            # Inference
            t1 = time_synchronized()
            with torch.no_grad():
                pred = model(img, augment=opt.augment)[0]
            t2 = time_synchronized()

            # Apply NMS
            pred = non_max_suppression(pred, opt.conf_thres, opt.iou_thres, classes=opt.classes, agnostic=opt.agnostic_nms)
            t3 = time_synchronized()

            # Process detections
            for i, det in enumerate(pred):
                if webcam:
                    p, s, im0, frame = path[i], '%g: ' % i, im0s[i].copy(), dataset.count
                else:
                    p, s, im0, frame = path, '', im0s, getattr(dataset, 'frame', 0)

                if len(det):
                    det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                    for *xyxy, conf, cls in reversed(det):
                        if names[int(cls)] == "invoice":
                            x1, y1, x2, y2 = int(xyxy[0]), int(xyxy[1]), int(xyxy[2]), int(xyxy[3])
                            invoice_roi = im0[y1:y2, x1:x2]

                            # Convert the invoice ROI to HSV and create a mask for the blue region
                            hsv = cv2.cvtColor(invoice_roi, cv2.COLOR_BGR2HSV)
                            lower_blue = np.array([90, 50, 50])
                            upper_blue = np.array([130, 255, 255])
                            mask = cv2.inRange(hsv, lower_blue, upper_blue)

                            # Morphological transformation to clean up mask
                            kernel = np.ones((3, 3), np.uint8)
                            mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel, iterations=2)

                            # Find contours in the cleaned mask
                            contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
                            bounding_boxes = [
                                cv2.boundingRect(contour) for contour in contours if cv2.contourArea(contour) > 200
                            ]
                            bounding_boxes = sorted(bounding_boxes, key=lambda b: (b[1], b[0]))

                            # OCR results storage
                            extracted_texts = []
                            for x, y, w, h in bounding_boxes:
                                roi = invoice_roi[y:y+h, x:x+w]

                                # EasyOCR을 사용하여 텍스트 인식
                                result = reader.readtext(roi, detail=0)
                                text = " ".join(result).strip()
                                extracted_texts.append(text)

                            # Send OCR result as JSON
                            if len(extracted_texts) >= 10:
                                data = {
                                    "senderName": extracted_texts[0], "recipientName": extracted_texts[1],
                                    "senderAddress": extracted_texts[2], "recipientAddress": extracted_texts[3],
                                    "productName": extracted_texts[4], "productQuantity": extracted_texts[5],
                                    "unitPrice": extracted_texts[6], "totalPrice": extracted_texts[7],
                                    "barNumber": extracted_texts[8], "regionCode": extracted_texts[9]
                                }
                                
                                print("추출된 JSON 데이터:", data)
                                url = "http://your-server-endpoint.com/api/upload"
                                try:
                                    response = requests.post(url, json=data, timeout=10)
                                    if response.status_code == 200:
                                        print("데이터가 성공적으로 전송되었습니다.")
                                    else:
                                        print(f"데이터 전송 실패: {response.status_code}")
                                except Exception as e:
                                    print(f"데이터 전송 중 오류 발생: {e}")

    except Exception as e:
        print(f"오류 발생: {e}")
    finally:
        cv2.destroyAllWindows()

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--weights', nargs='+', type=str, default='best.pt', help='model.pt path(s)')
    parser.add_argument('--source', type=str, default='inference/images', help='source')  # file/folder, 0 for webcam
    parser.add_argument('--img-size', type=int, default=640, help='inference size (pixels)')
    parser.add_argument('--conf-thres', type=float, default=0.25, help='object confidence threshold')
    parser.add_argument('--iou-thres', type=float, default=0.45, help='IOU threshold for NMS')
    parser.add_argument('--device', default='', help='cuda device, i.e. 0 or 0,1,2,3 or cpu')
    parser.add_argument('--view-img', action='store_true', help='display results')
    parser.add_argument('--save-txt', action='store_true', help='save results to *.txt')
    parser.add_argument('--save-conf', action='store_true', help='save confidences in --save-txt labels')
    parser.add_argument('--nosave', action='store_true', help='do not save images/videos')
    parser.add_argument('--classes', nargs='+', type=int, help='filter by class: --class 0, or --class 0 2 3')
    parser.add_argument('--agnostic-nms', action='store_true', help='class-agnostic NMS')
    parser.add_argument('--augment', action='store_true', help='augmented inference')
    parser.add_argument('--project', default='runs/detect', help='save results to project/name')
    parser.add_argument('--name', default='exp', help='save results to project/name')
    parser.add_argument('--exist-ok', action='store_true', help='existing project/name ok, do not increment')
    parser.add_argument('--no-trace', action='store_true', help='don`t trace model')
    opt = parser.parse_args()

    with torch.no_grad():
        detect_and_ocr()
