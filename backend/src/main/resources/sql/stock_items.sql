INSERT INTO product (product_name, quantity, product_status, unit_price, total_price, product_size, category_id,
                     invoice_id, area_id, product_location, expected_arrival_date)
VALUES
-- 전자제품 (category_id = 1)
('Galaxy watch', 5, 'PENDING_ARRIVAL', 816000, 4080000, 'SMALL', 1, 1, 1, 1, '2024-11-19'),
('Galaxy buds pro', 5, 'PENDING_ARRIVAL', 279000, 1395000, 'SMALL', 1, 2, 4, 1, '2024-11-19'),
('Galaxy Flip', 5, 'PENDING_ARRIVAL', 279000, 1395000, 'SMALL', 1, 3, 5, 1, '2024-11-19'),
('Galaxy Fold', 5, 'PENDING_ARRIVAL', 279000, 1395000, 'SMALL', 1, 4, 3, 1, '2024-11-19'),
('Galaxy ring', 8, 'PENDING_ARRIVAL', 469400, 3755200, 'SMALL', 1, 5, 2, 1, '2024-11-19'),

-- 식품 (category_id = 2)
('빵', 30, 'PENDING_ARRIVAL', 2000, 60000, 'SMALL', 2, 6, 2, 2, '2024-12-06'),
('우유', 50, 'PENDING_ARRIVAL', 1000, 50000, 'SMALL', 2, 7, 2, 2, '2024-12-07'),
('치즈', 25, 'LOADED', 500, 12500, 'SMALL', 2, 8, 2, 2, '2024-11-06'),
('양배추', 40, 'LOADED', 1000, 40000, 'SMALL', 2, 9, 2, 2, '2024-11-09'),
('사과', 35, 'PENDING_ARRIVAL', 1500, 52500, 'SMALL', 2, 10, 2, 2, '2024-12-10'),

-- 가구 (category_id = 3)
('쇼파', 1, 'PENDING_ARRIVAL', 800000, 800000, 'LARGE', 3, 11, 3, 3, '2024-12-17'),
('식탁 테이블', 1, 'LOADED', 450000, 450000, 'LARGE', 3, 12, 3, 3, '2024-11-03'),
('의자', 1, 'LOADED', 150000, 150000, 'SMALL', 3, 13, 3, 3, '2024-11-04'),
('침대', 1, 'PENDING_ARRIVAL', 2300000, 2300000, 'LARGE', 3, 14, 3, 3, '2024-12-14'),
('흔들의자', 1, 'PENDING_ARRIVAL', 18000, 18000, 'LARGE', 3, 15, 3, 3, '2024-12-15'),

-- 의류 (category_id = 4)
('티셔츠', 5, 'PENDING_ARRIVAL', 10000, 50000, 'SMALL', 4, 16, 4, 4, '2024-11-22'),
('청바지', 3, 'PENDING_ARRIVAL', 2000, 6000, 'SMALL', 4, 17, 4, 4, '2024-11-23'),
('재킷', 2, 'LOADED', 50000, 100000, 'LARGE', 4, 18, 4, 4, '2024-10-18'),
('신발', 1, 'LOADED', 6000, 6000, 'SMALL', 4, 19, 4, 4, '2024-10-20'),
('모자', 2, 'PENDING_ARRIVAL', 15000, 30000, 'SMALL', 4, 20, 4, 4, '2024-11-23'),

-- 스포츠용품 (category_id = 5)
('축구공', 10, 'PENDING_ARRIVAL', 30000, 300000, 'SMALL', 5, 21, 5, 5, '2024-12-21'),
('농구공', 8, 'PENDING_ARRIVAL', 20000, 160000, 'SMALL', 5, 22, 5, 5, '2024-12-22'),
('테니스 라켓', 12, 'LOADED', 10000, 120000, 'LARGE', 5, 23, 5, 5, '2024-11-02'),
('요가 매트', 1, 'LOADED', 250000, 250000, 'LARGE', 5, 24, 5, 5, '2024-11-01'),
('덤볼', 1, 'PENDING_ARRIVAL', 50000, 50000, 'LARGE', 5, 25, 5, 5, '2024-12-25'),

-- 도서 (category_id = 6)
('괭이부리말아이들', 30, 'PENDING_ARRIVAL', 12000, 360000, 'SMALL', 6, 26, 6, 6, '2024-11-26'),
('채식주의자', 20, 'PENDING_ARRIVAL', 18000, 360000, 'SMALL', 6, 27, 6, 6, '2024-12-27'),
('소년이간다', 10, 'PENDING_ARRIVAL', 10000, 100000, 'SMALL', 6, 28, 6, 6, '2024-12-28'),
('톰소여의모험', 5, 'LOADED', 15000, 75000, 'SMALL', 6, 29, 6, 6, '2024-11-15'),
('우리도 행복할 수 있을까', 25, 'PENDING_ARRIVAL', 22000, 550000, 'SMALL', 6, 30, 6, 6, '2024-12-30'),

-- 문구류 (category_id = 7)
('노트북', 50, 'PENDING_ARRIVAL', 3000, 150000, 'SMALL', 7, 31, 7, 7, '2024-11-21'),
('볼펜', 100, 'PENDING_ARRIVAL', 800, 80000, 'SMALL', 7, 32, 7, 7, '2025-01-02'),
('연필', 80, 'LOADED', 500, 40000, 'SMALL', 7, 33, 7, 7, '2024-11-03'),
('지우개', 60, 'LOADED', 700, 42000, 'SMALL', 7, 34, 7, 7, '2024-11-04'),
('자', 40, 'PENDING_ARRIVAL', 2000, 80000, 'SMALL', 7, 35, 7, 7, '2025-01-05'),

-- 생활용품 (category_id = 8)
('치약', 30, 'PENDING_ARRIVAL', 4000, 120000, 'SMALL', 8, 36, 8, 8, '2025-01-06'),
('칫솔', 60, 'PENDING_ARRIVAL', 3000, 180000, 'SMALL', 8, 37, 8, 8, '2025-01-07'),
('샴푸', 20, 'LOADED', 12000, 240000, 'LARGE', 8, 38, 8, 8, '2024-10-08'),
('비누', 40, 'LOADED', 6000, 240000, 'SMALL', 8, 39, 8, 8, '2024-11-15'),
('세제', 25, 'PENDING_ARRIVAL', 10000, 250000, 'LARGE', 8, 40, 8, 8, '2025-01-10'),

-- 화장품 (category_id = 9)
('파운데이션', 10, 'PENDING_ARRIVAL', 25000, 250000, 'SMALL', 9, 41, 9, 9, '2025-01-11'),
('립스틱', 15, 'PENDING_ARRIVAL', 15000, 225000, 'SMALL', 9, 42, 9, 9, '2025-01-12'),
('마스카라', 20, 'LOADED', 18000, 360000, 'SMALL', 9, 43, 9, 9, '2024-11-15'),
('아이라이너', 12, 'LOADED', 12000, 144000, 'SMALL', 9, 44, 9, 9, '2024-11-15'),
('블러셔', 18, 'PENDING_ARRIVAL', 25000, 450000, 'SMALL', 9, 45, 9, 9, '2025-01-15'),

-- 자동차용품 (category_id = 10)
('자동차 타이어', 4, 'PENDING_ARRIVAL', 500000, 2000000, 'LARGE', 10, 46, 10, 10, '2025-01-16'),
('차량 배터리', 5, 'PENDING_ARRIVAL', 600000, 3000000, 'LARGE', 10, 47, 10, 10, '2025-01-17'),
('엔진 오일', 10, 'LOADED', 50000, 500000, 'SMALL', 10, 48, 10, 10, '2024-11-15'),
('공기 주입기', 30, 'LOADED', 5000, 150000, 'SMALL', 10, 49, 10, 10, '2024-11-15'),
('워셔액', 20, 'PENDING_ARRIVAL', 15000, 300000, 'SMALL', 10, 50, 10, 10, '2025-01-20');