INSERT INTO product (product_name, quantity, product_status, unit_price, total_price, product_size, category_id,
                     invoice_id, area_id, product_location)
VALUES
-- 전자제품 (category_id = 1)
('Laptop', 10, 'PENDING_ARRIVAL', 1000, 10000, 'LARGE', 1, NULL, 1, 1),
('Smartphone', 20, 'PENDING_TRANSPORT', 500, 10000, 'SMALL', 1, NULL, 1, 1),
('Tablet', 15, 'IN_TRANSIT', 300, 4500, 'SMALL', 1, NULL, 1, 1),
('Television', 5, 'LOADED', 1500, 7500, 'LARGE', 1, NULL, 1, 1),
('Headphones', 25, 'PENDING_ARRIVAL', 200, 5000, 'SMALL', 1, NULL, 1, 1),

-- 식품 (category_id = 2)
('Bread', 30, 'PENDING_ARRIVAL', 2, 60, 'SMALL', 2, NULL, 2, 2),
('Milk', 50, 'PENDING_TRANSPORT', 1, 50, 'SMALL', 2, NULL, 2, 2),
('Cheese', 25, 'IN_TRANSIT', 5, 125, 'SMALL', 2, NULL, 2, 2),
('Vegetables', 40, 'LOADED', 3, 120, 'SMALL', 2, NULL, 2, 2),
('Fruit', 35, 'PENDING_ARRIVAL', 2, 70, 'SMALL', 2, NULL, 2, 2),

-- 가구 (category_id = 3)
('Sofa', 3, 'PENDING_TRANSPORT', 800, 2400, 'LARGE', 3, NULL, 3, 3),
('Dining Table', 2, 'IN_TRANSIT', 1200, 2400, 'LARGE', 3, NULL, 3, 3),
('Chair', 10, 'LOADED', 100, 1000, 'SMALL', 3, NULL, 3, 3),
('Bed', 1, 'PENDING_ARRIVAL', 1500, 1500, 'LARGE', 3, NULL, 3, 3),
('Wardrobe', 1, 'PENDING_TRANSPORT', 1800, 1800, 'LARGE', 3, NULL, 3, 3),

-- 의류 (category_id = 4)
('T-shirt', 50, 'PENDING_ARRIVAL', 10, 500, 'SMALL', 4, NULL, 4, 4),
('Jeans', 30, 'PENDING_TRANSPORT', 20, 600, 'SMALL', 4, NULL, 4, 4),
('Jacket', 20, 'IN_TRANSIT', 50, 1000, 'LARGE', 4, NULL, 4, 4),
('Shoes', 15, 'LOADED', 60, 900, 'SMALL', 4, NULL, 4, 4),
('Hat', 25, 'PENDING_ARRIVAL', 15, 375, 'SMALL', 4, NULL, 4, 4),

-- 스포츠용품 (category_id = 5)
('Football', 10, 'PENDING_ARRIVAL', 30, 300, 'SMALL', 5, NULL, 5, 5),
('Basketball', 8, 'PENDING_TRANSPORT', 35, 280, 'SMALL', 5, NULL, 5, 5),
('Tennis Racket', 12, 'IN_TRANSIT', 100, 1200, 'LARGE', 5, NULL, 5, 5),
('Yoga Mat', 20, 'LOADED', 25, 500, 'LARGE', 5, NULL, 5, 5),
('Dumbbell', 15, 'PENDING_ARRIVAL', 50, 750, 'LARGE', 5, NULL, 5, 5),

-- 도서 (category_id = 6)
('Book A', 30, 'PENDING_ARRIVAL', 10, 300, 'SMALL', 6, NULL, 6, 6),
('Book B', 20, 'PENDING_TRANSPORT', 15, 300, 'SMALL', 6, NULL, 6, 6),
('Magazine A', 10, 'IN_TRANSIT', 8, 80, 'SMALL', 6, NULL, 6, 6),
('Magazine B', 5, 'LOADED', 12, 60, 'SMALL', 6, NULL, 6, 6),
('Novel', 25, 'PENDING_ARRIVAL', 20, 500, 'SMALL', 6, NULL, 6, 6),

-- 문구류 (category_id = 7)
('Notebook', 50, 'PENDING_ARRIVAL', 2, 100, 'SMALL', 7, NULL, 7, 7),
('Pen', 100, 'PENDING_TRANSPORT', 1, 100, 'SMALL', 7, NULL, 7, 7),
('Pencil', 80, 'IN_TRANSIT', 1, 80, 'SMALL', 7, NULL, 7, 7),
('Eraser', 60, 'LOADED', 0.5, 30, 'SMALL', 7, NULL, 7, 7),
('Ruler', 40, 'PENDING_ARRIVAL', 1.5, 60, 'SMALL', 7, NULL, 7, 7),

-- 생활용품 (category_id = 8)
('Toothpaste', 30, 'PENDING_ARRIVAL', 3, 90, 'SMALL', 8, NULL, 8, 8),
('Toothbrush', 60, 'PENDING_TRANSPORT', 2, 120, 'SMALL', 8, NULL, 8, 8),
('Shampoo', 20, 'IN_TRANSIT', 10, 200, 'LARGE', 8, NULL, 8, 8),
('Soap', 40, 'LOADED', 5, 200, 'SMALL', 8, NULL, 8, 8),
('Detergent', 25, 'PENDING_ARRIVAL', 8, 200, 'LARGE', 8, NULL, 8, 8),

-- 화장품 (category_id = 9)
('Foundation', 10, 'PENDING_ARRIVAL', 20, 200, 'SMALL', 9, NULL, 9, 9),
('Lipstick', 15, 'PENDING_TRANSPORT', 15, 225, 'SMALL', 9, NULL, 9, 9),
('Mascara', 20, 'IN_TRANSIT', 18, 360, 'SMALL', 9, NULL, 9, 9),
('Eyeliner', 12, 'LOADED', 12, 144, 'SMALL', 9, NULL, 9, 9),
('Blush', 18, 'PENDING_ARRIVAL', 25, 450, 'SMALL', 9, NULL, 9, 9),

-- 자동차용품 (category_id = 10)
('Car Tire', 4, 'PENDING_ARRIVAL', 500, 2000, 'LARGE', 10, NULL, 10, 10),
('Car Battery', 5, 'PENDING_TRANSPORT', 600, 3000, 'LARGE', 10, NULL, 10, 10),
('Engine Oil', 10, 'IN_TRANSIT', 50, 500, 'SMALL', 10, NULL, 10, 10),
('Air Freshener', 30, 'LOADED', 5, 150, 'SMALL', 10, NULL, 10, 10),
('Car Wash Shampoo', 20, 'PENDING_ARRIVAL', 15, 300, 'SMALL', 10, NULL, 10, 10);
