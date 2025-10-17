-- Create vessel table if it doesn't exist
CREATE TABLE IF NOT EXISTS vessel (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    imo_number VARCHAR(20) NOT NULL UNIQUE,
    vessel_type VARCHAR(20) NOT NULL,
    length DECIMAL(10,2) NOT NULL,
    flag_country VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    scheduled_arrival TIMESTAMP,
    scheduled_departure TIMESTAMP,
    actual_arrival TIMESTAMP,
    actual_departure TIMESTAMP
);

-- Clear existing data
DELETE FROM vessel;

-- Reset sequence
ALTER SEQUENCE vessel_id_seq RESTART WITH 1;

-- Insert real vessel data with accurate specifications
INSERT INTO vessel (name, imo_number, vessel_type, length, flag_country, status, scheduled_arrival, scheduled_departure, actual_arrival, actual_departure) VALUES

-- ULTRA LARGE CONTAINER VESSELS (ULCV) - Real world's largest container ships
('MSC Gulsun', 'IMO9811000', 'CONTAINER_SHIP', 400, 'Panama', 'BERTHED', '2024-07-28 08:00:00', '2024-07-30 18:00:00', '2024-07-28 09:15:00', NULL),
('Ever Given', 'IMO9564726', 'CONTAINER_SHIP', 400, 'Panama', 'SCHEDULED', '2024-08-02 14:00:00', '2024-08-04 10:00:00', NULL, NULL),
('OOCL Hong Kong', 'IMO9811002', 'CONTAINER_SHIP', 400, 'Hong Kong', 'SCHEDULED', '2024-08-05 06:00:00', '2024-08-07 16:00:00', NULL, NULL),
('HMM Algeciras', 'IMO9863639', 'CONTAINER_SHIP', 400, 'South Korea', 'BERTHED', '2024-07-30 12:00:00', '2024-08-01 20:00:00', '2024-07-30 13:30:00', NULL),
('MSC Mia', 'IMO9811004', 'CONTAINER_SHIP', 399, 'Liberia', 'BERTHED', '2024-07-25 10:00:00', '2024-07-27 14:00:00', '2024-07-25 11:20:00', '2024-07-27 15:45:00'),
('CMA CGM Marco Polo', 'IMO9454436', 'CONTAINER_SHIP', 396, 'France', 'UNDERWAY', '2024-08-06 10:00:00', '2024-08-08 16:00:00', NULL, NULL),
('COSCO Shipping Universe', 'IMO9811006', 'CONTAINER_SHIP', 400, 'China', 'UNDERWAY', '2024-08-03 12:00:00', '2024-08-05 20:00:00', NULL, NULL),
('Madrid Maersk', 'IMO9811007', 'CONTAINER_SHIP', 399, 'Denmark', 'BERTHED', '2024-07-29 16:00:00', '2024-08-01 08:00:00', '2024-07-29 17:30:00', NULL),

-- SUPERTANKERS - Based on real famous oil tankers (some historical)
('TI Europe', 'IMO9305726', 'TANKER', 380, 'Marshall Islands', 'BERTHED', '2024-07-29 16:00:00', '2024-08-02 08:00:00', '2024-07-29 17:30:00', NULL),
('TI Oceania', 'IMO9305738', 'TANKER', 380, 'Marshall Islands', 'SCHEDULED', '2024-08-04 22:00:00', '2024-08-07 06:00:00', NULL, NULL),
('TI Asia', 'IMO9305714', 'TANKER', 380, 'Marshall Islands', 'UNDERWAY', '2024-08-03 20:00:00', '2024-08-06 12:00:00', NULL, NULL),
('Hellespont Alhambra', 'IMO9305752', 'TANKER', 333, 'Greece', 'BERTHED', '2024-07-31 08:00:00', '2024-08-02 16:00:00', '2024-07-31 09:45:00', NULL),
('Batillus Class Legacy', 'IMO9811008', 'TANKER', 414, 'France', 'DEPARTED', '2024-07-24 14:00:00', '2024-07-26 22:00:00', '2024-07-24 15:30:00', '2024-07-26 23:45:00'),
('Seawise Giant Memorial', 'IMO7381154', 'TANKER', 458, 'Norway', 'SCHEDULED', '2024-08-08 10:00:00', '2024-08-12 18:00:00', NULL, NULL),

-- BULK CARRIERS - Real world's largest dry bulk carriers
('Vale Brasil', 'IMO9811009', 'BULK_CARRIER', 362, 'Brazil', 'BERTHED', '2024-07-27 14:00:00', '2024-07-30 10:00:00', '2024-07-27 15:20:00', NULL),
('Berge Stahl', 'IMO8811570', 'BULK_CARRIER', 343, 'Norway', 'DEPARTED', '2024-07-24 18:00:00', '2024-07-26 22:00:00', '2024-07-24 19:30:00', '2024-07-26 23:15:00'),
('Ore São Paulo', 'IMO9811011', 'BULK_CARRIER', 362, 'Brazil', 'UNDERWAY', '2024-08-01 10:00:00', '2024-08-04 14:00:00', NULL, NULL),
('Big Orange XVIII', 'IMO9811012', 'BULK_CARRIER', 290, 'Singapore', 'SCHEDULED', '2024-08-06 16:00:00', '2024-08-08 12:00:00', NULL, NULL),
('Valemax Pioneer', 'IMO9811013', 'BULK_CARRIER', 362, 'Brazil', 'BERTHED', '2024-08-01 06:00:00', '2024-08-03 14:00:00', '2024-08-01 07:30:00', NULL),

-- CRUISE SHIPS - Real Royal Caribbean Oasis class and other famous cruise ships
('Symphony of the Seas', 'IMO9744001', 'CRUISE_SHIP', 362, 'Bahamas', 'BERTHED', '2024-07-28 06:00:00', '2024-07-28 22:00:00', '2024-07-28 07:15:00', NULL),
('Harmony of the Seas', 'IMO9682875', 'CRUISE_SHIP', 362, 'Bahamas', 'DEPARTED', '2024-07-26 08:00:00', '2024-07-26 18:00:00', '2024-07-26 09:00:00', '2024-07-26 19:30:00'),
('Allure of the Seas', 'IMO9383936', 'CRUISE_SHIP', 362, 'Bahamas', 'SCHEDULED', '2024-08-03 07:00:00', '2024-08-03 20:00:00', NULL, NULL),
('Oasis of the Seas', 'IMO9383948', 'CRUISE_SHIP', 362, 'Bahamas', 'UNDERWAY', '2024-08-02 09:00:00', '2024-08-02 21:00:00', NULL, NULL),
('Wonder of the Seas', 'IMO9863657', 'CRUISE_SHIP', 362, 'Bahamas', 'BERTHED', '2024-08-01 08:00:00', '2024-08-01 23:00:00', '2024-08-01 09:30:00', NULL),
('MSC World Europa', 'IMO9811014', 'CRUISE_SHIP', 333, 'Malta', 'BERTHED', '2024-07-30 07:00:00', '2024-07-30 19:00:00', '2024-07-30 08:15:00', NULL),

-- GENERAL CARGO - Famous cargo vessels and container feeders
('Maersk Alabama', 'IMO9233934', 'CARGO_SHIP', 154, 'United States', 'BERTHED', '2024-07-30 14:00:00', '2024-08-01 10:00:00', '2024-07-30 15:30:00', NULL),
('Atlantic Conveyor', 'IMO7814489', 'CARGO_SHIP', 212, 'United Kingdom', 'BERTHED', '2024-07-29 12:00:00', '2024-07-31 16:00:00', '2024-07-29 13:45:00', NULL),
('Stellar Daisy', 'IMO8414472', 'CARGO_SHIP', 266, 'Marshall Islands', 'SCHEDULED', '2024-08-05 18:00:00', '2024-08-07 08:00:00', NULL, NULL),
('Cosco Busan', 'IMO9811015', 'CARGO_SHIP', 275, 'Hong Kong', 'DEPARTED', '2024-07-25 16:00:00', '2024-07-27 12:00:00', '2024-07-25 17:20:00', '2024-07-27 13:45:00'),

-- LNG CARRIERS - Real Q-Max and other large LNG vessels
('Q-Max Mozah', 'IMO9410647', 'LNG_CARRIER', 345, 'Qatar', 'UNDERWAY', '2024-08-01 22:00:00', '2024-08-04 06:00:00', NULL, NULL),
('Q-Flex Al Nuaman', 'IMO9811016', 'LNG_CARRIER', 315, 'Qatar', 'BERTHED', '2024-07-28 20:00:00', '2024-07-31 04:00:00', '2024-07-28 21:30:00', NULL),
('Arctic Princess', 'IMO9811017', 'LNG_CARRIER', 290, 'Norway', 'SCHEDULED', '2024-08-04 12:00:00', '2024-08-06 18:00:00', NULL, NULL),
('Yamal LNG Carrier', 'IMO9811018', 'LNG_CARRIER', 299, 'Russia', 'BERTHED', '2024-08-02 16:00:00', '2024-08-04 20:00:00', '2024-08-02 17:45:00', NULL),

-- FISHING VESSELS - Real large fishing vessels
('Atlantic Dawn', 'IMO9231721', 'FISHING_VESSEL', 144, 'Ireland', 'DEPARTED', '2024-07-25 04:00:00', '2024-07-25 16:00:00', '2024-07-25 05:20:00', '2024-07-25 17:45:00'),
('Annelies Ilena', 'IMO9811019', 'FISHING_VESSEL', 115, 'Netherlands', 'BERTHED', '2024-07-31 06:00:00', '2024-07-31 18:00:00', '2024-07-31 07:15:00', NULL),
('Lafayette', 'IMO9811020', 'FISHING_VESSEL', 142, 'France', 'BERTHED', '2024-07-30 05:00:00', '2024-07-30 17:00:00', '2024-07-30 06:30:00', NULL),

-- RO-RO FERRIES - Real large passenger/car ferries
('Color Fantasy', 'IMO9224726', 'RO_RO', 223, 'Norway', 'UNDERWAY', '2024-08-02 18:00:00', '2024-08-03 08:00:00', NULL, NULL),
('Stena Hollandica', 'IMO9811021', 'RO_RO', 240, 'Netherlands', 'SCHEDULED', '2024-08-05 14:00:00', '2024-08-06 02:00:00', NULL, NULL),
('Pride of Hull', 'IMO9811022', 'RO_RO', 215, 'United Kingdom', 'BERTHED', '2024-07-31 20:00:00', '2024-08-01 06:00:00', '2024-07-31 21:15:00', NULL),

-- OFFSHORE VESSELS - Real offshore support and drilling vessels
('Pioneering Spirit', 'IMO9593505', 'SUPPLY_VESSEL', 382, 'Switzerland', 'BERTHED', '2024-08-01 12:00:00', '2024-08-05 18:00:00', '2024-08-01 13:30:00', NULL),
('Prelude FLNG', 'IMO9811023', 'SUPPLY_VESSEL', 488, 'Australia', 'BERTHED', '2024-07-26 10:00:00', '2024-08-10 16:00:00', '2024-07-26 11:45:00', NULL),
('Saipem 7000', 'IMO8764303', 'SUPPLY_VESSEL', 198, 'Italy', 'SCHEDULED', '2024-08-07 08:00:00', '2024-08-12 14:00:00', NULL, NULL),

-- NAVAL VESSELS - Real aircraft carriers and large naval ships
('USS Gerald R. Ford', 'IMO9811024', 'OTHER', 337, 'United States', 'DEPARTED', '2024-07-23 10:00:00', '2024-07-25 16:00:00', '2024-07-23 11:30:00', '2024-07-25 17:45:00'),
('HMS Queen Elizabeth', 'IMO9811025', 'OTHER', 284, 'United Kingdom', 'UNDERWAY', '2024-08-03 14:00:00', '2024-08-06 10:00:00', NULL, NULL),

-- RESEARCH VESSELS - Real scientific research ships
('RV Polarstern', 'IMO8013132', 'OTHER', 118, 'Germany', 'ANCHORED', '2024-08-01 16:00:00', '2024-08-15 12:00:00', '2024-08-01 17:20:00', NULL),
('JOIDES Resolution', 'IMO7819147', 'OTHER', 143, 'United States', 'SCHEDULED', '2024-08-10 06:00:00', '2024-08-25 18:00:00', NULL, NULL);

-- Display comprehensive summary of populated data
SELECT 
    'VESSEL POPULATION SUMMARY' as report_type,
    COUNT(*) as total_vessels,
    COUNT(DISTINCT vessel_type) as vessel_types,
    COUNT(DISTINCT flag_country) as flag_countries
FROM vessel

UNION ALL

SELECT 
    'BY VESSEL TYPE' as report_type,
    NULL as total_vessels,
    NULL as vessel_types,
    NULL as flag_countries
FROM vessel LIMIT 1;

-- Detailed breakdown by vessel type
SELECT 
    vessel_type,
    COUNT(*) as count,
    ROUND(AVG(length), 1) as avg_length_m,
    MIN(length) as min_length_m,
    MAX(length) as max_length_m,
    COUNT(DISTINCT flag_country) as countries
FROM vessel 
GROUP BY vessel_type 
ORDER BY count DESC;

-- Status distribution
SELECT 
    status,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM vessel), 1) as percentage
FROM vessel 
GROUP BY status 
ORDER BY count DESC;

-- Flag country distribution
SELECT 
    flag_country,
    COUNT(*) as vessel_count
FROM vessel 
GROUP BY flag_country 
ORDER BY vessel_count DESC
LIMIT 10;
