import pytesseract
from PIL import Image

# Tesseract 경로 설정 (설치된 Tesseract 경로로 변경)
pytesseract.pytesseract.tesseract_cmd = r'C:/Program Files/Tesseract-OCR/tesseract.exe'

# 테스트 이미지로 OCR 실행
image = Image.open("realinvo.png")  # 테스트용 이미지
text = pytesseract.image_to_string(image, lang='eng')
print("OCR 결과:")
print(text)
