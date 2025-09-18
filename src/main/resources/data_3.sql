
-- Insert для User (пользователи)
INSERT INTO users (id, nick_name, first_name, fathers_name, last_name, email, phone, password, created_at, updated_at, role_id) VALUES
(1, 'admin_ivan', 'Иван', 'Петрович', 'Сидоров', 'admin@hotel.ru', '+79161234567', '$2a$10$rD6J2Z8V1TbQ2h5sK8lZJe', NOW(), NOW(), 1),
(2, 'manager_olga', 'Ольга', 'Сергеевна', 'Иванова', 'manager@hotel.ru', '+79162345678', '$2a$10$rD6J2Z8V1TbQ2h5sK8lZJe', NOW(), NOW(), 2),
(3, 'cook_andrey', 'Андрей', 'Михайлович', 'Петров', 'cook@hotel.ru', '+79163456789', '$2a$10$rD6J2Z8V1TbQ2h5sK8lZJe', NOW(), NOW(), 3),
(4, 'cleaner_maria', 'Мария', 'Александровна', 'Смирнова', 'cleaner@hotel.ru', '+79164567890', '$2a$10$rD6J2Z8V1TbQ2h5sK8lZJe', NOW(), NOW(), 4),
(5, 'kitchen_alex', 'Алексей', 'Дмитриевич', 'Кузнецов', 'kitchen@hotel.ru', '+79165678901', '$2a$10$rD6J2Z8V1TbQ2h5sK8lZJe', NOW(), NOW(), 5);

-- Insert для PromoCode (промокоды)
INSERT INTO promo_codes (id, code, discount_type, description, discount_value, valid_from, valid_until, is_active) VALUES
(1, 'SUMMER2024', 'PERCENT', 'скидка в виде процента от стоимости', 15.00, '2024-06-01', '2024-08-31', true),
(2, 'WELCOME1000', 'FIXED', 'фиксированая скидка', 1000.00, '2024-01-01', '2024-12-31', true),
(3, 'WINTER500', 'FIXED', 'фиксированая скидка', 500.00, '2024-12-01', '2025-02-28', false),
(4, 'LOYALTY10', 'PERCENT', 'скидка в виде процента от стоимости', 10.00, '2024-03-01', '2025-03-01', true),
(5, 'NEWYEAR25', 'PERCENT', 'скидка в виде процента от стоимости', 25.00, '2024-12-20', '2025-01-10', true);

