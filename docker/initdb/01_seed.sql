-- Seed roles
INSERT INTO role (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ADMIN');

INSERT INTO role (name)
SELECT 'RECEPTIONIST'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'RECEPTIONIST');

-- Seed room types
INSERT INTO room_type (name, capacity, base_price)
SELECT 'Single', 1, 100
WHERE NOT EXISTS (SELECT 1 FROM room_type WHERE name = 'Single');

INSERT INTO room_type (name, capacity, base_price)
SELECT 'Double', 2, 180
WHERE NOT EXISTS (SELECT 1 FROM room_type WHERE name = 'Double');

-- Seed rooms referencing the room types above
INSERT INTO room (room_number, room_type_id, room_status)
SELECT '101', (SELECT id FROM room_type WHERE name = 'Single'), 'AVAILABLE'
WHERE NOT EXISTS (SELECT 1 FROM room WHERE room_number = '101');

INSERT INTO room (room_number, room_type_id, room_status)
SELECT '102', (SELECT id FROM room_type WHERE name = 'Single'), 'AVAILABLE'
WHERE NOT EXISTS (SELECT 1 FROM room WHERE room_number = '102');
