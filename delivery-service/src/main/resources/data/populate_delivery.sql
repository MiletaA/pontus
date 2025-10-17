-- Create delivery table if it doesn't exist
CREATE TABLE IF NOT EXISTS delivery (
    id BIGSERIAL PRIMARY KEY,
    vessel_id BIGINT NOT NULL,
    cargo_id BIGINT NOT NULL,
    dock_id BIGINT NOT NULL,
    delivery_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    actual_time TIMESTAMP,
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clear existing data
DELETE FROM delivery;

-- Reset sequence
ALTER SEQUENCE delivery_id_seq RESTART WITH 1;

-- Insert real delivery data representing various types of deliveries
-- vessel_id, cargo_id, dock_id correspond to their respective tables  
INSERT INTO delivery (vessel_id, cargo_id, dock_id, delivery_type, status, scheduled_time, actual_time, notes, created_at, updated_at) VALUES

-- Sample delivery records
(1, 1, 1, 'LOADING', 'COMPLETED', '2024-07-28 09:00:00', '2024-07-28 11:00:00', 'Container cargo loading completed', '2024-07-28 08:00:00', '2024-07-28 11:00:00'),
(1, 2, 1, 'LOADING', 'COMPLETED', '2024-07-28 11:00:00', '2024-07-28 13:00:00', 'Refrigerated cargo loading completed', '2024-07-28 08:00:00', '2024-07-28 13:00:00'),
(1, 3, 1, 'LOADING', 'IN_PROGRESS', '2024-07-30 08:00:00', NULL, 'Hazardous cargo loading scheduled', '2024-07-28 08:00:00', '2024-07-30 08:00:00'),
(2, 4, 2, 'LOADING', 'SCHEDULED', '2024-08-02 14:00:00', NULL, 'Container cargo loading scheduled', '2024-08-02 08:00:00', '2024-08-02 08:00:00'),
(2, 5, 2, 'LOADING', 'SCHEDULED', '2024-08-02 16:00:00', NULL, 'High-value cargo loading scheduled', '2024-08-02 08:00:00', '2024-08-02 08:00:00'),
(4, 9, 3, 'UNLOADING', 'COMPLETED', '2024-07-30 14:00:00', '2024-07-30 17:00:00', 'Electronics unloaded successfully', '2024-07-30 13:00:00', '2024-07-30 17:00:00'),
(4, 10, 3, 'UNLOADING', 'COMPLETED', '2024-07-30 17:00:00', '2024-07-30 19:00:00', 'Food products unloaded', '2024-07-30 13:00:00', '2024-07-30 19:00:00'),
(5, 11, 4, 'UNLOADING', 'COMPLETED', '2024-07-27 12:00:00', '2024-07-27 15:00:00', 'Textile cargo unloaded', '2024-07-27 11:00:00', '2024-07-27 15:00:00'),
(9, 13, 5, 'LOADING', 'COMPLETED', '2024-07-29 17:00:00', '2024-07-29 20:00:00', 'Crude oil loaded', '2024-07-29 16:00:00', '2024-07-29 20:00:00'),
(13, 18, 6, 'UNLOADING', 'COMPLETED', '2024-07-27 15:00:00', '2024-07-27 18:00:00', 'Bulk cargo unloaded', '2024-07-27 14:00:00', '2024-07-27 18:00:00'),
(18, 23, 7, 'LOADING', 'COMPLETED', '2024-07-28 07:00:00', '2024-07-28 08:00:00', 'Cruise supplies loaded', '2024-07-28 06:00:00', '2024-07-28 08:00:00'),
(23, 29, 8, 'UNLOADING', 'COMPLETED', '2024-07-30 15:00:00', '2024-07-30 17:00:00', 'General cargo unloaded', '2024-07-30 14:00:00', '2024-07-30 17:00:00'),
(8, 14, 9, 'LOADING', 'SCHEDULED', '2024-07-29 18:00:00', NULL, 'Container loading scheduled', '2024-07-29 17:00:00', '2024-07-29 17:00:00'),
(17, 19, 10, 'LOADING', 'SCHEDULED', '2024-08-01 08:00:00', NULL, 'Bulk cargo loading scheduled', '2024-08-01 07:00:00', '2024-08-01 07:00:00'),
(21, 25, 11, 'LOADING', 'COMPLETED', '2024-08-01 09:00:00', '2024-08-01 10:00:00', 'Cruise supplies loaded', '2024-08-01 08:00:00', '2024-08-01 10:00:00');

-- ADDITIONAL SCHEDULED AND IN-TRANSIT DELIVERIES
(6, 'Airbus Manufacturing Plant, Aircraft Parts Facility C-21, 21129 Hamburg, Germany', 'TRUCK', 'IN_TRANSIT', '2024-08-06 12:00:00', '2024-08-07 10:00:00', '2024-08-06 12:15:00', NULL, 'Jürgen Hoffmann', 'Volvo FH16 Aerospace Transport - License: HH-AS-2468', 'DEL-2024-001021', '2024-08-06 11:00:00', '2024-08-06 12:15:00'),
(7, 'Siemens Industrial Complex, Technology Hub D-14, 91052 Erlangen, Germany', 'TRUCK', 'IN_TRANSIT', '2024-08-03 14:00:00', '2024-08-04 18:00:00', '2024-08-03 14:20:00', NULL, 'Ralf Schneider', 'Mercedes Arocs Technology Transport - License: ER-TC-1357', 'DEL-2024-001022', '2024-08-03 13:00:00', '2024-08-03 14:20:00'),
(35, 'Volkswagen Assembly Plant, Automotive Production Line 2, 38440 Wolfsburg, Germany', 'RAIL', 'SCHEDULED', '2024-08-08 06:00:00', '2024-08-10 14:00:00', NULL, NULL, 'DB Cargo Automotive', 'Automotive Parts Train - ID: DB-AP-4681', 'DEL-2024-001023', '2024-08-06 12:00:00', '2024-08-06 12:00:00'),
(36, 'SAP Technology Campus, Software Distribution Center E-9, 69190 Walldorf, Germany', 'TRUCK', 'SCHEDULED', '2024-08-04 10:00:00', '2024-08-05 16:00:00', NULL, NULL, 'Christian Weber', 'DAF CF 440 IT Equipment - License: HD-IT-9753', 'DEL-2024-001024', '2024-08-03 14:00:00', '2024-08-03 14:00:00'),

-- FAILED DELIVERIES (For realistic scenarios)
(37, 'Bosch Manufacturing Plant, Industrial Equipment Facility F-6, 70469 Stuttgart, Germany', 'TRUCK', 'FAILED', '2024-08-02 08:00:00', '2024-08-03 16:00:00', '2024-08-02 08:30:00', NULL, 'Matthias Klein', 'Scania R500 Industrial Transport - License: S-IN-8642', 'DEL-2024-001025', '2024-08-02 07:00:00', '2024-08-03 18:00:00'),

-- PENDING PICKUPS
(14, 'Evonik Chemical Distribution, Specialty Chemicals Hub G-11, 45128 Essen, Germany', 'HAZMAT_TRUCK', 'PENDING', '2024-08-08 10:00:00', '2024-08-09 18:00:00', NULL, NULL, 'Frank Richter', 'MAN TGX Hazmat Certified - License: E-HZ-5791', 'DEL-2024-001026', '2024-08-04 16:00:00', '2024-08-04 16:00:00'),
(15, 'ArcelorMittal Steel Works, Heavy Industry Complex H-3, 47166 Duisburg, Germany', 'RAIL', 'PENDING', '2024-08-09 04:00:00', '2024-08-11 20:00:00', NULL, NULL, 'DB Cargo Heavy Industry', 'Heavy Freight Train - ID: DB-HF-3579', 'DEL-2024-001027', '2024-08-01 12:00:00', '2024-08-01 12:00:00');

-- Verification queries
SELECT 
    status,
    COUNT(*) as delivery_count
FROM delivery 
GROUP BY status
ORDER BY status;

SELECT 
    delivery_type,
    COUNT(*) as delivery_count
FROM delivery 
GROUP BY delivery_type
ORDER BY delivery_count DESC;

SELECT 
    DATE(scheduled_delivery) as delivery_date,
    COUNT(*) as deliveries_scheduled
FROM delivery 
WHERE scheduled_delivery IS NOT NULL
GROUP BY DATE(scheduled_delivery)
ORDER BY delivery_date;
