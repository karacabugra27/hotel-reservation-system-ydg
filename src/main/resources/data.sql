-- ROOM TYPE SEED
INSERT INTO room_type (id, name, capacity, base_price)
VALUES
    (1, 'Single', 1, 1000),
    (2, 'Double', 2, 1500);

-- ROOM SEED
INSERT INTO room (id, room_number, room_type_id, room_status)
VALUES
    (1, '101', 1, 'AVAILABLE'),
    (2, '102', 2, 'AVAILABLE'),
    (3, '103', 1, 'OCCUPIED');