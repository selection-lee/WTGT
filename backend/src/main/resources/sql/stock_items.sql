INSERT INTO product (product_name, quantity, product_status, unit_price, total_price, product_size, category_id,
                     invoice_id, area_id, product_location, expected_arrival_date)
VALUES
-- 전자제품 (category_id = 1)
('노트북', 10, 'PENDING_ARRIVAL', 1000, 10000, 'LARGE', 1, 1, 1, 1, '2024-11-05'),
('스마트폰', 20, 'PENDING_TRANSPORT', 500, 10000, 'SMALL', 1, 2, 1, 1, '2024-12-02'),
('태블릿', 15, 'IN_TRANSIT', 300, 4500, 'SMALL', 1, 3, 1, 1, '2024-12-03'),
('텔레비전', 5, 'LOADED', 1500, 7500, 'LARGE', 1, 4, 1, 1, '2024-12-04'),
('헤드폰', 25, 'PENDING_ARRIVAL', 200, 5000, 'SMALL', 1, 5, 1, 1, '2024-11-05'),

-- 식품 (category_id = 2)
('빵', 30, 'PENDING_ARRIVAL', 2, 60, 'SMALL', 2, 6, 2, 2, '2024-12-06'),
('우유', 50, 'PENDING_TRANSPORT', 1, 50, 'SMALL', 2, 7, 2, 2, '2024-12-07'),
('치즈', 25, 'IN_TRANSIT', 5, 125, 'SMALL', 2, 8, 2, 2, '2024-11-06'),
('요구르트', 40, 'LOADED', 3, 120, 'SMALL', 2, 9, 2, 2, '2024-12-09'),
('딸기', 35, 'PENDING_ARRIVAL', 2, 70, 'SMALL', 2, 10, 2, 2, '2024-12-10'),

-- 가구 (category_id = 3)
('쇼파', 3, 'PENDING_TRANSPORT', 800, 2400, 'LARGE', 3, 11, 3, 3, '2024-11-07'),
('식탁 테이블', 2, 'IN_TRANSIT', 1200, 2400, 'LARGE', 3, 12, 3, 3, '2024-12-12'),
('의자', 10, 'LOADED', 100, 1000, 'SMALL', 3, 13, 3, 3, '2024-12-13'),
('침대', 1, 'PENDING_ARRIVAL', 1500, 1500, 'LARGE', 3, 14, 3, 3, '2024-12-14'),
('책상', 1, 'PENDING_TRANSPORT', 1800, 1800, 'LARGE', 3, 15, 3, 3, '2024-12-15'),

-- 의류 (category_id = 4)
('티셔츠', 50, 'PENDING_ARRIVAL', 10, 500, 'SMALL', 4, 16, 4, 4, '2024-12-16'),
('청바지', 30, 'PENDING_TRANSPORT', 20, 600, 'SMALL', 4, 17, 4, 4, '2024-12-17'),
('가죽재킷', 20, 'IN_TRANSIT', 50, 1000, 'LARGE', 4, 18, 4, 4, '2024-12-18'),
('신발', 15, 'LOADED', 60, 900, 'SMALL', 4, 19, 4, 4, '2024-12-19'),
('모자', 25, 'PENDING_ARRIVAL', 15, 375, 'SMALL', 4, 20, 4, 4, '2024-12-20'),

-- 스포츠용품 (category_id = 5)
('축구공', 10, 'PENDING_ARRIVAL', 30, 300, 'SMALL', 5, 21, 5, 5, '2024-12-21'),
('농구공', 8, 'PENDING_TRANSPORT', 35, 280, 'SMALL', 5, 22, 5, 5, '2024-12-22'),
('테니스 라켓', 12, 'IN_TRANSIT', 100, 1200, 'LARGE', 5, 23, 5, 5, '2024-12-23'),
('요가 매트', 20, 'LOADED', 25, 500, 'LARGE', 5, 24, 5, 5, '2024-12-24'),
('볼링공', 15, 'PENDING_ARRIVAL', 50, 750, 'LARGE', 5, 25, 5, 5, '2024-12-25'),

-- 도서 (category_id = 6)
('타이탄의 도구들', 30, 'PENDING_ARRIVAL', 10, 300, 'SMALL', 6, 26, 6, 6, '2024-12-26'),
('반지의 제왕', 20, 'PENDING_TRANSPORT', 15, 300, 'SMALL', 6, 27, 6, 6, '2024-12-27'),
('컨택트', 10, 'IN_TRANSIT', 8, 80, 'SMALL', 6, 28, 6, 6, '2024-12-28'),
('데미안', 5, 'LOADED', 12, 60, 'SMALL', 6, 29, 6, 6, '2024-12-29'),
('톰 소여의 모험', 25, 'PENDING_ARRIVAL', 20, 500, 'SMALL', 6, 30, 6, 6, '2024-12-30'),

-- 문구류 (category_id = 7)
('공책', 50, 'PENDING_ARRIVAL', 2, 100, 'SMALL', 7, 31, 7, 7, '2025-01-01'),
('볼펜', 100, 'PENDING_TRANSPORT', 1, 100, 'SMALL', 7, 32, 7, 7, '2025-01-02'),
('연필', 80, 'IN_TRANSIT', 1, 80, 'SMALL', 7, 33, 7, 7, '2025-01-03'),
('지우개', 60, 'LOADED', 0.5, 30, 'SMALL', 7, 34, 7, 7, '2025-01-04'),
('자', 40, 'PENDING_ARRIVAL', 1.5, 60, 'SMALL', 7, 35, 7, 7, '2025-01-05'),

-- 생활용품 (category_id = 8)
('Toothpaste', 30, 'PENDING_ARRIVAL', 3, 90, 'SMALL', 8, 36, 8, 8, '2025-01-06'),
('Toothbrush', 60, 'PENDING_TRANSPORT', 2, 120, 'SMALL', 8, 37, 8, 8, '2025-01-07'),
('Shampoo', 20, 'IN_TRANSIT', 10, 200, 'LARGE', 8, 38, 8, 8, '2025-01-08'),
('Soap', 40, 'LOADED', 5, 200, 'SMALL', 8, 39, 8, 8, '2025-01-09'),
('Detergent', 25, 'PENDING_ARRIVAL', 8, 200, 'LARGE', 8, 40, 8, 8, '2025-01-10'),

-- 화장품 (category_id = 9)
('Foundation', 10, 'PENDING_ARRIVAL', 20, 200, 'SMALL', 9, 41, 9, 9, '2025-01-11'),
('Lipstick', 15, 'PENDING_TRANSPORT', 15, 225, 'SMALL', 9, 42, 9, 9, '2025-01-12'),
('Mascara', 20, 'IN_TRANSIT', 18, 360, 'SMALL', 9, 43, 9, 9, '2025-01-13'),
('Eyeliner', 12, 'LOADED', 12, 144, 'SMALL', 9, 44, 9, 9, '2025-01-14'),
('Blush', 18, 'PENDING_ARRIVAL', 25, 450, 'SMALL', 9, 45, 9, 9, '2025-01-15'),

-- 자동차용품 (category_id = 10)
('Car Tire', 4, 'PENDING_ARRIVAL', 500, 2000, 'LARGE', 10, 46, 10, 10, '2025-01-16'),
('Car Battery', 5, 'PENDING_TRANSPORT', 600, 3000, 'LARGE', 10, 47, 10, 10, '2025-01-17'),
('Engine Oil', 10, 'IN_TRANSIT', 50, 500, 'SMALL', 10, 48, 10, 10, '2025-01-18'),
('Air Freshener', 30, 'LOADED', 5, 150, 'SMALL', 10, 49, 10, 10, '2025-01-19'),
('Car Wash Shampoo', 20, 'PENDING_ARRIVAL', 15, 300, 'SMALL', 10, 50, 10, 10, '2025-01-20');
