
-- Insert для Booking (бронирования)
INSERT INTO bookings (check_in_date, check_out_date, persons, total_price, status, status_description, created_at, updated_at, room_id, user_id, promo_code_id) VALUES
('2026-06-15', '2026-06-20', 2, 12500.00, 'CONFIRMED', 'бронирование подтверждено', NOW(), NOW(), 1, 1, 1),
('2026-07-01', '2026-07-10', 3, 40500.00, 'CONFIRMED', 'запрос на подтверждение брони', NOW(), NOW(), 2, 2, 2),
('2026-08-05', '2026-08-15', 4, 90000.00, 'CANCELLED', 'бронирование отменено', NOW(), NOW(), 3, 3, NULL),
('2026-09-10', '2026-09-12', 2, 24000.00, 'CONFIRMED', 'бронирование подтверждено', NOW(), NOW(), 4, 4, 4),
('2026-10-20', '2026-10-25', 1, 11500.00, 'CONFIRMED', 'запрос на подтверждение брони', NOW(), NOW(), 5, 5, 5),
('2026-11-01', '2026-11-05', 2, 9200.00, 'CONFIRMED', 'бронирование подтверждено', NOW(), NOW(), 6, 6, 6),
('2026-12-20', '2026-12-27', 4, 140000.00, 'CONFIRMED', 'бронирование подтверждено', NOW(), NOW(), 8, 7, 5),
('2026-01-10', '2026-01-12', 1, 3600.00, 'CONFIRMED', 'запрос на подтверждение брони', NOW(), NOW(), 9, 8, 9),
('2026-02-14', '2026-02-16', 2, 16000.00, 'CONFIRMED', 'бронирование подтверждено', NOW(), NOW(), 7, 9, 10),
('2026-03-05', '2026-03-10', 3, 27500.00, 'CONFIRMED', 'запрос на подтверждение брони', NOW(), NOW(), 10, 10, 7);


-- Insert для Report (отчеты)
INSERT INTO reports (status, created_at, updated_at, room_id, staff_id) VALUES
('SUBMITTED', NOW(), NOW(), 1, 4),
('APPROVED', NOW(), NOW(), 2, 4),
('REJECTED', NOW(), NOW(), 3, 4),
('FIXED', NOW(), NOW(), 4, 4),
('ISSUE', NOW(), NOW(), 5, 4),
('SUBMITTED', NOW(), NOW(), 6, 9),
('APPROVED', NOW(), NOW(), 7, 9),
('REJECTED', NOW(), NOW(), 8, 9),
('FIXED', NOW(), NOW(), 9, 9),
('ISSUE', NOW(), NOW(), 10, 9);




