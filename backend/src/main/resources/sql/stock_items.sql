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
('쇼파', 3, 'PENDING_ARRIVAL', 800, 2400, 'LARGE', 3, 11, 3, 3, '2024-12-17'),
('식탁 테이블', 2, 'LOADED', 1200, 2400, 'LARGE', 3, 12, 3, 3, '2024-11-03'),
('의자', 10, 'LOADED', 100, 1000, 'SMALL', 3, 13, 3, 3, '2024-11-04'),
('침대', 1, 'PENDING_ARRIVAL', 1500, 1500, 'LARGE', 3, 14, 3, 3, '2024-12-14'),
('흔들의자', 1, 'PENDING_ARRIVAL', 1800, 1800, 'LARGE', 3, 15, 3, 3, '2024-12-15'),

-- 의류 (category_id = 4)
('티셔츠', 50, 'PENDING_ARRIVAL', 10, 500, 'SMALL', 4, 16, 4, 4, '2024-11-22'),
('청바지', 30, 'PENDING_ARRIVAL', 20, 600, 'SMALL', 4, 17, 4, 4, '2024-11-23'),
('재킷', 20, 'LOADED', 50, 1000, 'LARGE', 4, 18, 4, 4, '2024-10-18'),
('신발', 15, 'LOADED', 60, 900, 'SMALL', 4, 19, 4, 4, '2024-10-20'),
('모자', 25, 'PENDING_ARRIVAL', 15, 375, 'SMALL', 4, 20, 4, 4, '2024-11-23'),

-- 스포츠용품 (category_id = 5)
('축구공', 10, 'PENDING_ARRIVAL', 30, 300, 'SMALL', 5, 21, 5, 5, '2024-12-21'),
('농구공', 8, 'PENDING_ARRIVAL', 35, 280, 'SMALL', 5, 22, 5, 5, '2024-12-22'),
('테니스 라켓', 12, 'LOADED', 100, 1200, 'LARGE', 5, 23, 5, 5, '2024-11-02'),
('요가 매트', 20, 'LOADED', 25, 500, 'LARGE', 5, 24, 5, 5, '2024-11-01'),
('덤볼', 15, 'PENDING_ARRIVAL', 50, 750, 'LARGE', 5, 25, 5, 5, '2024-12-25'),

-- 도서 (category_id = 6)
('괭이부리말아이들', 30, 'PENDING_ARRIVAL', 10, 300, 'SMALL', 6, 26, 6, 6, '2024-11-26'),
('채식주의자', 20, 'PENDING_ARRIVAL', 15, 300, 'SMALL', 6, 27, 6, 6, '2024-12-27'),
('소년이간다', 10, 'PENDING_ARRIVAL', 8, 80, 'SMALL', 6, 28, 6, 6, '2024-12-28'),
('톰소여의모험', 5, 'LOADED', 12, 60, 'SMALL', 6, 29, 6, 6, '2024-11-15'),
('우리도 행복할 수 있을까', 25, 'PENDING_ARRIVAL', 20, 500, 'SMALL', 6, 30, 6, 6, '2024-12-30'),

-- 문구류 (category_id = 7)
('노트북', 50, 'PENDING_ARRIVAL', 2, 100, 'SMALL', 7, 31, 7, 7, '2024-11-21'),
('볼펜', 100, 'PENDING_ARRIVAL', 1, 100, 'SMALL', 7, 32, 7, 7, '2025-01-02'),
('연필', 80, 'LOADED', 1, 80, 'SMALL', 7, 33, 7, 7, '2024-11-03'),
('지우개', 60, 'LOADED', 0.5, 30, 'SMALL', 7, 34, 7, 7, '2024-11-04'),
('자', 40, 'PENDING_ARRIVAL', 1.5, 60, 'SMALL', 7, 35, 7, 7, '2025-01-05'),

-- 생활용품 (category_id = 8)
('치약', 30, 'PENDING_ARRIVAL', 3, 90, 'SMALL', 8, 36, 8, 8, '2025-01-06'),
('칫솔', 60, 'PENDING_ARRIVAL', 2, 120, 'SMALL', 8, 37, 8, 8, '2025-01-07'),
('샴푸', 20, 'LOADED', 10, 200, 'LARGE', 8, 38, 8, 8, '2024-10-08'),
('Soap', 40, 'LOADED', 5, 200, 'SMALL', 8, 39, 8, 8, '2024-11-15'),
('Detergent', 25, 'PENDING_ARRIVAL', 8, 200, 'LARGE', 8, 40, 8, 8, '2025-01-10'),

-- 화장품 (category_id = 9)
('Foundation', 10, 'PENDING_ARRIVAL', 20, 200, 'SMALL', 9, 41, 9, 9, '2025-01-11'),
('Lipstick', 15, 'PENDING_ARRIVAL', 15, 225, 'SMALL', 9, 42, 9, 9, '2025-01-12'),
('Mascara', 20, 'LOADED', 18, 360, 'SMALL', 9, 43, 9, 9, '2024-11-15'),
('Eyeliner', 12, 'LOADED', 12, 144, 'SMALL', 9, 44, 9, 9, '2024-11-15'),
('Blush', 18, 'PENDING_ARRIVAL', 25, 450, 'SMALL', 9, 45, 9, 9, '2025-01-15'),

-- 자동차용품 (category_id = 10)
('Car Tire', 4, 'PENDING_ARRIVAL', 500, 2000, 'LARGE', 10, 46, 10, 10, '2025-01-16'),
('Car Battery', 5, 'PENDING_ARRIVAL', 600, 3000, 'LARGE', 10, 47, 10, 10, '2025-01-17'),
('Engine Oil', 10, 'LOADED', 50, 500, 'SMALL', 10, 48, 10, 10, '2024-11-15'),
('Air Freshener', 30, 'LOADED', 5, 150, 'SMALL', 10, 49, 10, 10, '2024-11-15'),
('Car Wash Shampoo', 20, 'PENDING_ARRIVAL', 15, 300, 'SMALL', 10, 50, 10, 10, '2025-01-20');
