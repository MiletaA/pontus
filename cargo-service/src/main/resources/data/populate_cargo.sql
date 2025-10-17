-- Create cargo table if it doesn't exist
CREATE TABLE IF NOT EXISTS cargo (
    id BIGSERIAL PRIMARY KEY,
    vessel_id BIGINT NOT NULL,
    description VARCHAR(1000) NOT NULL,
    weight_tons DECIMAL(10,2) NOT NULL,
    is_dangerous BOOLEAN NOT NULL DEFAULT FALSE,
    cargo_type VARCHAR(50) NOT NULL,
    origin_port VARCHAR(100),
    destination_port VARCHAR(100),
    loaded_at TIMESTAMP,
    unloaded_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clear existing data
DELETE FROM cargo;

-- Reset sequence
ALTER SEQUENCE cargo_id_seq RESTART WITH 1;

-- Insert real cargo data representing various types of maritime cargo
-- vessel_id corresponds to vessels from the vessel table
INSERT INTO cargo (vessel_id, description, weight_tons, is_dangerous, customs_status, cargo_type, created_at, updated_at) VALUES

-- CONTAINER CARGO (MSC Gulsun - vessel_id 1)
(1, '2,400 TEU Mixed Container Cargo - Electronics, Textiles, Automotive Parts', 28800.00, false, 'CLEARED', 'CONTAINER', '2024-07-28 09:30:00', '2024-07-28 11:45:00'),
(1, '850 TEU Refrigerated Containers - Fresh Produce, Pharmaceuticals', 10200.00, false, 'CLEARED', 'REFRIGERATED', '2024-07-28 09:45:00', '2024-07-28 12:00:00'),
(1, '120 TEU Dangerous Goods Containers - Chemicals Class 3 & 8', 1440.00, true, 'PENDING', 'HAZARDOUS', '2024-07-28 10:00:00', '2024-07-28 10:00:00'),

-- CONTAINER CARGO (Ever Given - vessel_id 2)
(2, '3,200 TEU General Cargo - Consumer Goods, Machinery, Raw Materials', 38400.00, false, 'PENDING', 'CONTAINER', '2024-08-02 08:00:00', '2024-08-02 08:00:00'),
(2, '1,100 TEU High-Value Cargo - Electronics, Luxury Goods', 13200.00, false, 'PENDING', 'GENERAL_CARGO', '2024-08-02 08:15:00', '2024-08-02 08:15:00'),
(2, '200 TEU Dangerous Goods - Lithium Batteries, Flammable Liquids', 2400.00, true, 'PENDING', 'HAZARDOUS', '2024-08-02 08:30:00', '2024-08-02 08:30:00'),

-- CONTAINER CARGO (OOCL Hong Kong - vessel_id 3)
(3, '2,800 TEU Mixed Manufacturing Goods - Textiles, Plastics, Metal Products', 33600.00, false, 'PENDING', 'CONTAINER', '2024-08-05 06:00:00', '2024-08-05 06:00:00'),
(3, '900 TEU Automotive Parts and Vehicles - Car Components, Motorcycles', 10800.00, false, 'PENDING', 'AUTOMOTIVE', '2024-08-05 06:15:00', '2024-08-05 06:15:00'),

-- CONTAINER CARGO (HMM Algeciras - vessel_id 4)
(4, '3,500 TEU Consumer Electronics - Smartphones, Computers, Appliances', 42000.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-30 13:45:00', '2024-07-30 15:30:00'),
(4, '750 TEU Food Products - Canned Goods, Beverages, Snacks', 9000.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-30 14:00:00', '2024-07-30 15:45:00'),
(4, '150 TEU Chemical Products - Industrial Chemicals Class 6.1', 1800.00, true, 'CLEARED', 'HAZARDOUS', '2024-07-30 14:15:00', '2024-07-30 16:00:00'),

-- CONTAINER CARGO (MSC Mia - vessel_id 5)
(5, '2,200 TEU Textile and Apparel - Clothing, Fabrics, Footwear', 26400.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-25 11:30:00', '2024-07-27 14:20:00'),
(5, '600 TEU Home and Garden - Furniture, Tools, Decorative Items', 7200.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-25 11:45:00', '2024-07-27 14:35:00'),

-- CRUDE OIL CARGO (TI Europe - vessel_id 9)
(9, 'Crude Oil - Brent Blend 320,000 MT from North Sea', 320000.00, true, 'CLEARED', 'BULK_LIQUID', '2024-07-29 18:00:00', '2024-07-29 20:30:00'),

-- CRUDE OIL CARGO (TI Oceania - vessel_id 10)
(10, 'Crude Oil - West Texas Intermediate 315,000 MT', 315000.00, true, 'PENDING', 'BULK_LIQUID', '2024-08-04 22:00:00', '2024-08-04 22:00:00'),

-- CRUDE OIL CARGO (TI Asia - vessel_id 11)
(11, 'Crude Oil - Dubai Crude 310,000 MT from Middle East', 310000.00, true, 'PENDING', 'BULK_LIQUID', '2024-08-03 20:00:00', '2024-08-03 20:00:00'),

-- CHEMICAL CARGO (Hellespont Alhambra - vessel_id 12)
(12, 'Refined Petroleum Products - Gasoline, Diesel, Jet Fuel 280,000 MT', 280000.00, true, 'CLEARED', 'BULK_LIQUID', '2024-07-31 10:00:00', '2024-07-31 12:45:00'),

-- BULK CARGO (Vale Brasil - vessel_id 13)
(13, 'Iron Ore - High Grade Hematite 380,000 MT from Brazil', 380000.00, false, 'CLEARED', 'BULK_DRY', '2024-07-27 15:30:00', '2024-07-27 18:20:00'),

-- BULK CARGO (Berge Stahl - vessel_id 14)
(14, 'Iron Ore - Magnetite Concentrate 365,000 MT', 365000.00, false, 'CLEARED', 'BULK_DRY', '2024-07-24 19:45:00', '2024-07-26 22:30:00'),

-- BULK CARGO (Ore São Paulo - vessel_id 15)
(15, 'Iron Ore - Pellets and Fines 375,000 MT for Steel Production', 375000.00, false, 'PENDING', 'BULK_DRY', '2024-08-01 10:00:00', '2024-08-01 10:00:00'),

-- BULK CARGO (Big Orange XVIII - vessel_id 16)
(16, 'Coal - Thermal Coal 220,000 MT for Power Generation', 220000.00, false, 'PENDING', 'BULK_DRY', '2024-08-06 16:00:00', '2024-08-06 16:00:00'),

-- BULK CARGO (Valemax Pioneer - vessel_id 17)
(17, 'Iron Ore - Premium Grade 385,000 MT with Low Impurities', 385000.00, false, 'CLEARED', 'BULK_DRY', '2024-08-01 07:45:00', '2024-08-01 09:30:00'),

-- PASSENGER CARGO (Symphony of the Seas - vessel_id 18)
(18, 'Cruise Supplies - Food, Beverages, Amenities for 6,680 Passengers', 2500.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-28 07:30:00', '2024-07-28 08:45:00'),
(18, 'Duty-Free Merchandise - Alcohol, Perfumes, Jewelry, Souvenirs', 150.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-28 07:45:00', '2024-07-28 09:00:00'),

-- PASSENGER CARGO (Harmony of the Seas - vessel_id 19)
(19, 'Cruise Provisions - Fresh Food, Linens, Entertainment Equipment', 2200.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-26 09:15:00', '2024-07-26 11:30:00'),

-- PASSENGER CARGO (Wonder of the Seas - vessel_id 21)
(21, 'Cruise Operations Cargo - Fuel, Water, Food for 7,000 Passengers', 2800.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-08-01 09:45:00', '2024-08-01 11:15:00'),

-- GENERAL CARGO (Maersk Alabama - vessel_id 23)
(23, 'Mixed General Cargo - Agricultural Equipment, Machinery Parts', 8500.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-30 15:45:00', '2024-07-30 17:20:00'),

-- GENERAL CARGO (Atlantic Conveyor - vessel_id 24)
(24, 'Military Cargo - Vehicles, Equipment, Supplies (NATO Mission)', 12000.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-29 08:30:00', '2024-07-29 10:45:00'),

-- RO-RO CARGO (Stena Hollandica - vessel_id 25)
(25, 'Roll-on/Roll-off Cargo - 180 Trucks, 45 Trailers, 220 Cars', 4200.00, false, 'CLEARED', 'AUTOMOTIVE', '2024-07-28 14:15:00', '2024-07-28 16:30:00'),

-- LIVESTOCK CARGO (Livestock Express - vessel_id 26)
(26, 'Live Cattle - 2,500 Head of Premium Angus Cattle for Export', 1250.00, false, 'CLEARED', 'LIVESTOCK', '2024-07-27 11:20:00', '2024-07-27 13:45:00'),

-- GRAIN CARGO (Grain Master - vessel_id 27)
(27, 'Wheat - Premium Grade 45,000 MT for Food Processing', 45000.00, false, 'CLEARED', 'BULK_DRY', '2024-07-26 16:45:00', '2024-07-26 19:20:00'),

-- TIMBER CARGO (Forest Pioneer - vessel_id 28)
(28, 'Lumber - Softwood Logs and Processed Timber 25,000 MT', 25000.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-25 13:30:00', '2024-07-25 16:15:00'),

-- ADDITIONAL MIXED CARGO FOR VARIOUS VESSELS
(6, '1,800 TEU Mixed Cargo - Industrial Equipment, Raw Materials', 21600.00, false, 'PENDING', 'GENERAL_CARGO', '2024-08-06 10:00:00', '2024-08-06 10:00:00'),
(7, '2,600 TEU Consumer Goods - Household Items, Sports Equipment', 31200.00, false, 'PENDING', 'GENERAL_CARGO', '2024-08-03 12:00:00', '2024-08-03 12:00:00'),
(8, '2,100 TEU Technology Products - Computers, Telecommunications', 25200.00, false, 'CLEARED', 'GENERAL_CARGO', '2024-07-29 17:45:00', '2024-07-29 19:30:00'),

-- DANGEROUS GOODS SAMPLES
(20, 'Cruise Emergency Supplies - Medical Oxygen, Safety Equipment', 80.00, true, 'PENDING', 'HAZARDOUS', '2024-08-03 07:00:00', '2024-08-03 07:00:00'),
(22, 'Cruise Maintenance - Paint, Solvents, Cleaning Chemicals', 120.00, true, 'CLEARED', 'HAZARDOUS', '2024-08-01 08:15:00', '2024-08-01 10:30:00');

-- Verification queries
SELECT 
    customs_status,
    COUNT(*) as cargo_count,
    SUM(weight_tons) as total_weight
FROM cargo 
GROUP BY customs_status
ORDER BY customs_status;

SELECT 
    cargo_type,
    COUNT(*) as cargo_count,
    AVG(weight_tons) as avg_weight
FROM cargo 
GROUP BY cargo_type
ORDER BY cargo_count DESC;

SELECT 
    is_dangerous,
    COUNT(*) as cargo_count,
    SUM(weight_tons) as total_weight
FROM cargo 
GROUP BY is_dangerous;
