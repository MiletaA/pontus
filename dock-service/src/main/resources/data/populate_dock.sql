-- Create dock table if it doesn't exist
CREATE TABLE IF NOT EXISTS dock (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    max_length DECIMAL(10,2) NOT NULL,
    is_occupied BOOLEAN NOT NULL DEFAULT FALSE,
    assigned_vessel_id BIGINT,
    scheduled_from TIMESTAMP,
    scheduled_to TIMESTAMP,
    handles_dangerous BOOLEAN NOT NULL DEFAULT FALSE,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clear existing data
DELETE FROM dock;

-- Reset sequence
ALTER SEQUENCE dock_id_seq RESTART WITH 1;

-- Insert real dock data representing various types of maritime infrastructure
-- Note: assigned_vessel_id will correspond to vessel IDs from the vessel table
INSERT INTO dock (name, max_length, is_occupied, assigned_vessel_id, scheduled_from, scheduled_to, handles_dangerous, description, created_at, updated_at) VALUES

-- ULTRA LARGE CONTAINER VESSEL BERTHS (400m+ capacity)
('ULCV Terminal A-1', 450.0, true, 1, '2024-07-28 08:00:00', '2024-07-30 18:00:00', true, 'Ultra Large Container Vessel berth with 22 STS cranes, 18m depth, handles hazardous containers', '2024-01-15 10:00:00', '2024-07-28 09:15:00'),
('ULCV Terminal A-2', 450.0, false, NULL, NULL, NULL, true, 'Ultra Large Container Vessel berth with 20 STS cranes, 18m depth, automated gate system', '2024-01-15 10:30:00', '2024-07-20 14:20:00'),
('ULCV Terminal A-3', 450.0, true, 4, '2024-07-30 12:00:00', '2024-08-01 20:00:00', true, 'Ultra Large Container Vessel berth with 24 STS cranes, cold storage facilities', '2024-01-15 11:00:00', '2024-07-30 13:30:00'),
('ULCV Terminal A-4', 450.0, true, 8, '2024-07-29 16:00:00', '2024-08-01 08:00:00', false, 'Ultra Large Container Vessel berth with 18 STS cranes, general cargo only', '2024-01-15 11:30:00', '2024-07-29 17:30:00'),

-- LARGE CONTAINER VESSEL BERTHS (300-400m)
('Container Terminal B-1', 380.0, false, NULL, '2024-08-02 14:00:00', '2024-08-04 10:00:00', false, 'Large container vessel berth with 16 STS cranes, rail connection', '2024-01-16 09:00:00', '2024-07-25 16:45:00'),
('Container Terminal B-2', 380.0, false, NULL, '2024-08-05 06:00:00', '2024-08-07 16:00:00', true, 'Large container vessel berth with 14 STS cranes, dangerous goods certified', '2024-01-16 09:30:00', '2024-07-28 11:30:00'),
('Container Terminal B-3', 380.0, false, NULL, '2024-08-06 10:00:00', '2024-08-08 16:00:00', false, 'Large container vessel berth with 12 STS cranes, reefer connections', '2024-01-16 10:00:00', '2024-07-30 09:15:00'),
('Container Terminal B-4', 380.0, false, NULL, '2024-08-03 12:00:00', '2024-08-05 20:00:00', true, 'Large container vessel berth with 15 STS cranes, customs bonded area', '2024-01-16 10:30:00', '2024-07-31 13:20:00'),

-- SUPERTANKER TERMINALS (400m+ with specialized equipment)
('Oil Terminal T-1', 460.0, true, 9, '2024-07-29 16:00:00', '2024-08-02 08:00:00', true, 'Supertanker berth with vapor recovery system, 8 loading arms, fire suppression', '2024-01-17 08:00:00', '2024-07-29 17:30:00'),
('Oil Terminal T-2', 460.0, false, NULL, '2024-08-04 22:00:00', '2024-08-07 06:00:00', true, 'Supertanker berth with ballast water treatment, 6 loading arms, spill response', '2024-01-17 08:30:00', '2024-08-01 07:45:00'),
('Oil Terminal T-3', 460.0, false, NULL, '2024-08-03 20:00:00', '2024-08-06 12:00:00', true, 'Supertanker berth with heating system, 10 loading arms, nitrogen blanketing', '2024-01-17 09:00:00', '2024-07-29 15:10:00'),
('Chemical Terminal CT-1', 380.0, true, 12, '2024-07-31 08:00:00', '2024-08-02 16:00:00', true, 'Chemical tanker berth with specialized pumping systems, inert gas system', '2024-01-17 09:30:00', '2024-07-31 09:45:00'),

-- BULK CARGO TERMINALS (Large dry bulk carriers)
('Bulk Terminal D-1', 370.0, true, 13, '2024-07-27 14:00:00', '2024-07-30 10:00:00', false, 'Iron ore terminal with ship loaders, conveyor system, dust suppression', '2024-01-18 07:00:00', '2024-07-27 15:20:00'),
('Bulk Terminal D-2', 370.0, false, NULL, '2024-08-01 10:00:00', '2024-08-04 14:00:00', false, 'Coal terminal with rotary dumpers, rail connections, covered storage', '2024-01-18 07:30:00', '2024-07-27 12:30:00'),
('Bulk Terminal D-3', 350.0, false, NULL, '2024-08-06 16:00:00', '2024-08-08 12:00:00', false, 'Grain terminal with pneumatic systems, silo storage, quality testing lab', '2024-01-18 08:00:00', '2024-08-01 10:45:00'),
('Bulk Terminal D-4', 370.0, true, 17, '2024-08-01 06:00:00', '2024-08-03 14:00:00', false, 'Bauxite terminal with grab cranes, covered conveyors, rail loading', '2024-01-18 08:30:00', '2024-08-01 07:30:00'),

-- CRUISE TERMINALS (Large passenger vessels)
('Cruise Terminal C-1', 380.0, true, 18, '2024-07-28 06:00:00', '2024-07-28 22:00:00', false, 'Luxury cruise terminal with passenger boarding bridges, duty-free shopping', '2024-01-19 09:00:00', '2024-07-28 07:15:00'),
('Cruise Terminal C-2', 380.0, false, NULL, '2024-08-03 07:00:00', '2024-08-03 20:00:00', false, 'Modern cruise terminal with customs facilities, taxi/bus connections', '2024-01-19 09:30:00', '2024-07-30 14:20:00'),
('Cruise Terminal C-3', 380.0, false, NULL, '2024-08-02 09:00:00', '2024-08-02 21:00:00', false, 'Historic cruise terminal with heritage features, panoramic views', '2024-01-19 10:00:00', '2024-07-31 16:15:00'),
('Cruise Terminal C-4', 350.0, true, 22, '2024-08-01 08:00:00', '2024-08-01 23:00:00', false, 'Premium cruise terminal with VIP lounges, cultural exhibitions', '2024-01-19 10:30:00', '2024-08-01 09:30:00'),

-- LNG TERMINALS (Specialized for LNG carriers)
('LNG Terminal L-1', 350.0, false, NULL, '2024-08-01 22:00:00', '2024-08-04 06:00:00', true, 'LNG terminal with cryogenic loading arms, regasification plant', '2024-01-20 08:00:00', '2024-07-28 18:30:00'),
('LNG Terminal L-2', 350.0, true, 26, '2024-07-28 20:00:00', '2024-07-31 04:00:00', true, 'LNG terminal with storage tanks, vapor recovery, safety systems', '2024-01-20 08:30:00', '2024-07-28 21:30:00'),
('LNG Terminal L-3', 320.0, false, NULL, '2024-08-04 12:00:00', '2024-08-06 18:00:00', true, 'Small-scale LNG terminal with truck loading facilities', '2024-01-20 09:00:00', '2024-07-29 11:45:00'),

-- GENERAL CARGO BERTHS (Multi-purpose terminals)
('General Cargo G-1', 250.0, true, 23, '2024-07-30 14:00:00', '2024-08-01 10:00:00', false, 'Multi-purpose terminal with mobile cranes, heavy lift capability', '2024-01-21 07:00:00', '2024-07-30 15:30:00'),
('General Cargo G-2', 280.0, true, 24, '2024-07-29 12:00:00', '2024-07-31 16:00:00', true, 'Project cargo terminal with heavy lift cranes, dangerous goods storage', '2024-01-21 07:30:00', '2024-07-29 13:45:00'),
('General Cargo G-3', 200.0, false, NULL, '2024-08-05 18:00:00', '2024-08-07 08:00:00', false, 'Break bulk terminal with covered warehouses, fork lift access', '2024-01-21 08:00:00', '2024-08-01 08:20:00'),
('General Cargo G-4', 220.0, false, NULL, NULL, NULL, true, 'Hazardous cargo terminal with specialized handling equipment', '2024-01-21 08:30:00', '2024-07-30 13:10:00'),

-- RO-RO TERMINALS (Roll-on/Roll-off ferries)
('RO-RO Terminal R-1', 250.0, false, NULL, '2024-08-02 18:00:00', '2024-08-03 08:00:00', false, 'Ferry terminal with adjustable ramps, passenger facilities', '2024-01-22 09:00:00', '2024-07-31 15:25:00'),
('RO-RO Terminal R-2', 280.0, false, NULL, '2024-08-05 14:00:00', '2024-08-06 02:00:00', false, 'Car carrier terminal with multi-level ramps, customs inspection', '2024-01-22 09:30:00', '2024-07-29 12:40:00'),
('RO-RO Terminal R-3', 230.0, true, 31, '2024-07-31 20:00:00', '2024-08-01 06:00:00', false, 'Truck ferry terminal with driver facilities, fuel station', '2024-01-22 10:00:00', '2024-07-31 21:15:00'),

-- OFFSHORE SUPPORT BERTHS (Specialized offshore vessels)
('Offshore Terminal O-1', 400.0, true, 32, '2024-08-01 12:00:00', '2024-08-05 18:00:00', true, 'Heavy lift vessel berth with specialized equipment for offshore operations', '2024-01-23 08:00:00', '2024-08-01 13:30:00'),
('Offshore Terminal O-2', 500.0, true, 33, '2024-07-26 10:00:00', '2024-08-10 16:00:00', true, 'FLNG terminal with cryogenic facilities, ultra-deep water berth', '2024-01-23 08:30:00', '2024-07-26 11:45:00'),
('Offshore Terminal O-3', 220.0, false, NULL, '2024-08-07 08:00:00', '2024-08-12 14:00:00', true, 'Offshore construction vessel berth with pipe laying equipment', '2024-01-23 09:00:00', '2024-07-30 17:05:00'),

-- NAVAL BERTHS (Military vessels when visiting)
('Naval Berth N-1', 350.0, false, NULL, NULL, NULL, false, 'Naval vessel berth with security clearance, restricted access', '2024-01-24 10:00:00', '2024-08-01 09:30:00'),
('Naval Berth N-2', 300.0, false, NULL, '2024-08-03 14:00:00', '2024-08-06 10:00:00', false, 'Naval support berth with fueling capabilities, communications', '2024-01-24 10:30:00', '2024-07-28 14:55:00'),

-- FISHING VESSEL BERTHS
('Fishing Berth F-1', 160.0, false, NULL, NULL, NULL, false, 'Commercial fishing berth with ice plant, fish market access', '2024-01-25 06:00:00', '2024-07-31 06:15:00'),
('Fishing Berth F-2', 130.0, true, 30, '2024-07-31 06:00:00', '2024-07-31 18:00:00', false, 'Fishing vessel berth with gear storage, fuel dock', '2024-01-25 06:30:00', '2024-07-31 07:15:00'),
('Fishing Berth F-3', 150.0, true, 31, '2024-07-30 05:00:00', '2024-07-30 17:00:00', false, 'Large fishing vessel berth with processing facilities', '2024-01-25 07:00:00', '2024-07-30 06:30:00'),

-- RESEARCH VESSEL BERTHS
('Research Berth S-1', 140.0, true, 37, '2024-08-01 16:00:00', '2024-08-15 12:00:00', false, 'Scientific research berth with laboratory connections, data links', '2024-01-26 08:00:00', '2024-08-01 17:20:00'),
('Research Berth S-2', 160.0, false, NULL, '2024-08-10 06:00:00', '2024-08-25 18:00:00', false, 'Deep sea research berth with specialized equipment support', '2024-01-26 08:30:00', '2024-08-01 11:20:00'),

-- REPAIR AND MAINTENANCE BERTHS
('Dry Dock DD-1', 400.0, false, NULL, NULL, NULL, true, 'Large dry dock for vessel repairs, painting, hull maintenance', '2024-01-27 07:00:00', '2024-07-29 18:45:00'),
('Dry Dock DD-2', 250.0, false, NULL, NULL, NULL, false, 'Medium dry dock for routine maintenance and surveys', '2024-01-27 07:30:00', '2024-07-30 05:40:00'),
('Repair Berth RP-1', 300.0, false, NULL, NULL, NULL, true, 'Floating repair berth with crane access, workshop facilities', '2024-01-27 08:00:00', '2024-07-31 19:25:00'),

-- BUNKERING BERTHS (Fuel supply)
('Bunker Berth B-1', 200.0, false, NULL, NULL, NULL, true, 'Marine fuel supply berth with multiple fuel grades, safety systems', '2024-01-28 07:00:00', '2024-08-01 12:10:00'),
('Bunker Berth B-2', 180.0, false, NULL, NULL, NULL, true, 'LNG bunkering berth with cryogenic fuel systems', '2024-01-28 07:30:00', '2024-07-28 20:30:00'),

-- QUARANTINE BERTHS (Health inspection)
('Quarantine Berth Q-1', 300.0, false, NULL, NULL, NULL, false, 'Quarantine berth for health inspections, isolated from main port', '2024-01-29 08:00:00', '2024-07-30 22:15:00'),
('Quarantine Berth Q-2', 250.0, false, NULL, NULL, NULL, false, 'Secondary quarantine berth with medical facilities', '2024-01-29 08:30:00', '2024-07-31 21:45:00'),

-- ANCHORAGE AREAS (Virtual berths for anchored vessels)
('Anchorage Area A-1', 500.0, false, NULL, NULL, NULL, true, 'Main anchorage area for vessels awaiting berth assignment', '2024-01-30 00:00:00', '2024-07-29 16:20:00'),
('Anchorage Area A-2', 500.0, false, NULL, NULL, NULL, false, 'Secondary anchorage area for non-hazardous cargo vessels', '2024-01-30 00:00:00', '2024-08-01 14:35:00'),
('Anchorage Area A-3', 500.0, false, NULL, NULL, NULL, true, 'Dangerous goods anchorage area, isolated location', '2024-01-30 00:00:00', '2024-07-30 10:25:00');

-- Display comprehensive summary of populated data
SELECT 
    'DOCK POPULATION SUMMARY' as report_type,
    COUNT(*) as total_docks,
    COUNT(CASE WHEN is_occupied = true THEN 1 END) as occupied_docks,
    COUNT(CASE WHEN is_occupied = false THEN 1 END) as available_docks,
    ROUND(AVG(max_length), 1) as avg_max_length_m
FROM dock;

-- Detailed breakdown by dock type (based on naming pattern)
SELECT 
    CASE 
        WHEN name LIKE 'ULCV%' THEN 'Ultra Large Container'
        WHEN name LIKE 'Container%' THEN 'Large Container'
        WHEN name LIKE 'Oil Terminal%' THEN 'Oil Tanker'
        WHEN name LIKE 'Chemical%' THEN 'Chemical Tanker'
        WHEN name LIKE 'Bulk%' THEN 'Bulk Cargo'
        WHEN name LIKE 'Cruise%' THEN 'Cruise Ship'
        WHEN name LIKE 'LNG%' THEN 'LNG Terminal'
        WHEN name LIKE 'General%' THEN 'General Cargo'
        WHEN name LIKE 'RO-RO%' THEN 'RO-RO Ferry'
        WHEN name LIKE 'Offshore%' THEN 'Offshore Support'
        WHEN name LIKE 'Naval%' THEN 'Naval'
        WHEN name LIKE 'Fishing%' THEN 'Fishing'
        WHEN name LIKE 'Research%' THEN 'Research'
        WHEN name LIKE 'Dry Dock%' OR name LIKE 'Repair%' THEN 'Repair/Maintenance'
        WHEN name LIKE 'Bunker%' THEN 'Bunkering'
        WHEN name LIKE 'Quarantine%' THEN 'Quarantine'
        WHEN name LIKE 'Anchorage%' THEN 'Anchorage'
        ELSE 'Other'
    END as dock_type,
    COUNT(*) as dock_count,
    COUNT(CASE WHEN is_occupied = true THEN 1 END) as occupied_count,
    ROUND(AVG(max_length), 1) as avg_length_m,
    MIN(max_length) as min_length_m,
    MAX(max_length) as max_length_m,
    COUNT(CASE WHEN handles_dangerous = true THEN 1 END) as dangerous_goods_capable
FROM dock 
GROUP BY dock_type
ORDER BY dock_count DESC;

-- Occupancy status
SELECT 
    is_occupied,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM dock), 1) as percentage
FROM dock 
GROUP BY is_occupied;

-- Dangerous goods handling capability
SELECT 
    handles_dangerous,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM dock), 1) as percentage
FROM dock 
GROUP BY handles_dangerous;

-- Length distribution
SELECT 
    CASE 
        WHEN max_length < 150 THEN 'Small (<150m)'
        WHEN max_length < 250 THEN 'Medium (150-250m)'
        WHEN max_length < 350 THEN 'Large (250-350m)'
        WHEN max_length < 450 THEN 'Very Large (350-450m)'
        ELSE 'Ultra Large (450m+)'
    END as size_category,
    COUNT(*) as dock_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM dock), 1) as percentage
FROM dock 
GROUP BY size_category
ORDER BY MIN(max_length);
