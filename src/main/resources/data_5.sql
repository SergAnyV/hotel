
-- Insert для Booking (бронирования)
INSERT INTO bookings (id, check_in_date, check_out_date, persons, total_price, status, status_description, created_at, updated_at, room_id, user_id, promo_code_id) VALUES
(1, '2024-06-15', '2024-06-20', 2, 12500.00, 'CONFIRMED', 'бронирование подтвреждено', NOW(), NOW(), 1, 1, 1),
(2, '2024-07-01', '2024-07-10', 3, 40500.00, 'REQUEST', 'запрос на подтверждение брони', NOW(), NOW(), 2, 2, 2),
(3, '2024-08-05', '2024-08-15', 4, 90000.00, 'CANCELLED', 'бронирование отменено', NOW(), NOW(), 3, 3, NULL),
(4, '2024-09-10', '2024-09-12', 2, 24000.00, 'CONFIRMED', 'бронирование подтвреждено', NOW(), NOW(), 4, 4, 4),
(5, '2024-10-20', '2024-10-25', 1, 11500.00, 'REQUEST', 'запрос на подтверждение брони', NOW(), NOW(), 5, 5, 5);

-- Insert для Report (отчеты)
INSERT INTO reports (id, status, created_at, updated_at, room_id, staff_id) VALUES
(1, 'SUBMITTED', NOW(), NOW(), 1, 4),
(2, 'APPROVED', NOW(), NOW(), 2, 4),
(3, 'REJECTED', NOW(), NOW(), 3, 4),
(4, 'FIXED', NOW(), NOW(), 4, 4),
(5, 'ISSUE', NOW(), NOW(), 5, 4);




