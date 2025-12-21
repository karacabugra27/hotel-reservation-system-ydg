-- ROOM TYPE SEED
INSERT INTO room_type (id, name, capacity, base_price)
VALUES
    ( 'Single', 1, 1000),
    ( 'Double', 2, 1500);

-- ROOM SEED
INSERT INTO room (id, room_number, room_type_id, room_status)
VALUES
    ( '101', 1, 'AVAILABLE'),
    ('102', 2, 'AVAILABLE'),
    ( '103', 1, 'OCCUPIED');