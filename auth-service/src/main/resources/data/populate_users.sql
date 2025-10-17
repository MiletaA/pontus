-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clear existing data
DELETE FROM users;

-- Reset sequence
ALTER SEQUENCE users_id_seq RESTART WITH 1;

-- Insert real user data representing various maritime industry roles
INSERT INTO users (username, email, password, first_name, last_name, role, enabled, created_at, updated_at) VALUES

-- PORT AUTHORITY OFFICIALS
('harbormaster', 'h.anderson@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Harold', 'Anderson', 'HARBOR_MASTER', true, '2024-01-15 08:30:00', '2024-07-20 14:22:00'),
('dockmaster01', 'd.martinez@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Diego', 'Martinez', 'HARBOR_MASTER', true, '2024-01-15 09:00:00', '2024-07-25 16:45:00'),
('portcontrol', 'l.jensen@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Lars', 'Jensen', 'OPERATIONS', true, '2024-01-16 07:45:00', '2024-07-28 11:30:00'),
('trafficcoord', 's.petrova@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Svetlana', 'Petrova', 'OPERATIONS', true, '2024-01-16 08:15:00', '2024-07-30 09:15:00'),

-- VESSEL TRAFFIC SERVICE (VTS) OPERATORS
('vts_operator01', 'm.nakamura@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Masaki', 'Nakamura', 'OPERATIONS', true, '2024-01-17 06:00:00', '2024-07-31 13:20:00'),
('vts_operator02', 'a.olsson@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Astrid', 'Olsson', 'OPERATIONS', true, '2024-01-17 06:00:00', '2024-08-01 07:45:00'),
('vts_supervisor', 'r.thompson@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Robert', 'Thompson', 'MANAGER', true, '2024-01-17 06:30:00', '2024-07-29 15:10:00'),

-- CUSTOMS AND BORDER CONTROL
('customs_chief', 'p.dubois@customs.gov', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Pierre', 'Dubois', 'MANAGER', true, '2024-01-18 08:00:00', '2024-07-27 12:30:00'),
('customs_officer01', 'k.mueller@customs.gov', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Klaus', 'Mueller', 'CUSTOMS_OFFICER', true, '2024-01-18 08:30:00', '2024-08-01 10:45:00'),
('customs_officer02', 'i.rodriguez@customs.gov', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Isabella', 'Rodriguez', 'CUSTOMS_OFFICER', true, '2024-01-18 08:30:00', '2024-07-30 14:20:00'),
('customs_inspector', 'j.wong@customs.gov', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Jenny', 'Wong', 'CUSTOMS_OFFICER', true, '2024-01-19 07:30:00', '2024-07-31 16:15:00'),

-- CARGO OPERATIONS
('cargomaster', 'c.vanderberg@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Cornelius', 'van der Berg', 'MANAGER', true, '2024-01-20 07:00:00', '2024-07-28 18:30:00'),
('cargo_supervisor', 'n.popov@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Nikolai', 'Popov', 'OPERATIONS', true, '2024-01-20 07:30:00', '2024-07-29 11:45:00'),
('stevedore_chief', 'g.papadopoulos@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Georgios', 'Papadopoulos', 'MANAGER', true, '2024-01-21 06:00:00', '2024-08-01 08:20:00'),
('crane_operator01', 'h.kim@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Hyun-soo', 'Kim', 'DOCK_WORKER', true, '2024-01-21 06:30:00', '2024-07-30 13:10:00'),
('crane_operator02', 'f.silva@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Fernando', 'Silva', 'DOCK_WORKER', true, '2024-01-21 06:30:00', '2024-07-31 15:25:00'),

-- SHIPPING AGENTS
('agent_maersk', 'e.christensen@maersk-agency.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Erik', 'Christensen', 'OPERATIONS', true, '2024-01-22 08:00:00', '2024-07-29 12:40:00'),
('agent_msc', 'm.rossi@msc-agency.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Marco', 'Rossi', 'OPERATIONS', true, '2024-01-22 08:00:00', '2024-07-30 17:05:00'),
('agent_cosco', 'l.zhang@cosco-agency.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Li', 'Zhang', 'OPERATIONS', true, '2024-01-22 08:00:00', '2024-08-01 09:30:00'),
('agent_cma', 'a.bernard@cma-cgm-agency.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Antoine', 'Bernard', 'OPERATIONS', true, '2024-01-23 08:00:00', '2024-07-28 14:55:00'),

-- TUGBOAT CAPTAINS
('tugcaptain01', 'w.kowalski@pontustug.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Wojciech', 'Kowalski', 'VESSEL_CAPTAIN', true, '2024-01-24 05:00:00', '2024-07-31 06:15:00'),
('tugcaptain02', 'b.hansen@pontustug.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Bjorn', 'Hansen', 'VESSEL_CAPTAIN', true, '2024-01-24 05:00:00', '2024-08-01 11:20:00'),
('tugcaptain03', 's.petropoulos@pontustug.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Spyros', 'Petropoulos', 'VESSEL_CAPTAIN', true, '2024-01-24 05:00:00', '2024-07-29 18:45:00'),

-- PILOTS
('pilot01', 'j.mcconnell@pontuspilots.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'James', 'McConnell', 'VESSEL_CAPTAIN', true, '2024-01-25 04:30:00', '2024-07-30 05:40:00'),
('pilot02', 'r.fernandez@pontuspilots.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Ricardo', 'Fernandez', 'VESSEL_CAPTAIN', true, '2024-01-25 04:30:00', '2024-07-31 19:25:00'),
('pilot_chief', 'a.magnusson@pontuspilots.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Anders', 'Magnusson', 'HARBOR_MASTER', true, '2024-01-25 04:00:00', '2024-08-01 12:10:00'),

-- SECURITY PERSONNEL
('security_chief', 'r.johnson@pontussecurity.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Raymond', 'Johnson', 'MANAGER', true, '2024-01-26 07:00:00', '2024-07-28 20:30:00'),
('security_officer01', 't.novak@pontussecurity.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Tomasz', 'Novak', 'OPERATIONS', true, '2024-01-26 07:30:00', '2024-07-30 22:15:00'),
('security_officer02', 'f.garcia@pontussecurity.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Francisco', 'Garcia', 'OPERATIONS', true, '2024-01-26 07:30:00', '2024-07-31 21:45:00'),

-- MARINE SURVEYORS
('surveyor01', 'd.lloyd@pontussurvey.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'David', 'Lloyd', 'OPERATIONS', true, '2024-01-27 08:00:00', '2024-07-29 16:20:00'),
('surveyor02', 'y.tanaka@pontussurvey.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Yuki', 'Tanaka', 'OPERATIONS', true, '2024-01-27 08:00:00', '2024-08-01 14:35:00'),

-- LOGISTICS COORDINATORS
('logistics01', 'e.virtanen@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Eero', 'Virtanen', 'OPERATIONS', true, '2024-01-28 08:30:00', '2024-07-30 10:25:00'),
('logistics02', 'c.andersen@pontuslogistics.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Christina', 'Andersen', 'OPERATIONS', true, '2024-01-28 08:30:00', '2024-07-31 17:50:00'),

-- SYSTEM ADMINISTRATORS
('admin', 'admin@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'System', 'Administrator', 'ADMIN', true, '2024-01-10 00:00:00', '2024-08-01 23:59:00'),
('sysadmin', 'it.support@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Technical', 'Support', 'ADMIN', true, '2024-01-10 08:00:00', '2024-07-31 18:30:00'),

-- EMERGENCY RESPONSE
('emergency_coord', 'emg.coord@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Emergency', 'Coordinator', 'OPERATIONS', true, '2024-01-15 00:00:00', '2024-07-30 12:00:00'),

-- INACTIVE/TEST ACCOUNTS
('testuser01', 'test@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Test', 'User', 'USER', false, '2024-02-01 12:00:00', '2024-02-01 12:00:00'),
('deactivated_user', 'old.user@pontusport.com', '$2a$10$N9qo8uLOickgx2ZMRjKgqeBAaLw2iRdNLOfJK/PpFbk7wGdHzILu6', 'Former', 'Employee', 'USER', false, '2023-12-01 09:00:00', '2024-06-15 17:00:00');

-- Display comprehensive summary of populated data
SELECT 
    'USER POPULATION SUMMARY' as report_type,
    COUNT(*) as total_users,
    COUNT(DISTINCT role) as unique_roles,
    COUNT(CASE WHEN enabled = true THEN 1 END) as active_users,
    COUNT(CASE WHEN enabled = false THEN 1 END) as inactive_users
FROM users;

-- Detailed breakdown by role
SELECT 
    role,
    COUNT(*) as user_count,
    COUNT(CASE WHEN enabled = true THEN 1 END) as active_count,
    COUNT(CASE WHEN enabled = false THEN 1 END) as inactive_count,
    ROUND(COUNT(CASE WHEN enabled = true THEN 1 END) * 100.0 / COUNT(*), 1) as active_percentage
FROM users 
GROUP BY role 
ORDER BY user_count DESC;

-- Recent activity summary
SELECT 
    'RECENT ACTIVITY' as report_type,
    COUNT(CASE WHEN updated_at >= CURRENT_DATE - INTERVAL '7 days' THEN 1 END) as updated_last_week,
    COUNT(CASE WHEN updated_at >= CURRENT_DATE - INTERVAL '30 days' THEN 1 END) as updated_last_month,
    COUNT(CASE WHEN created_at >= CURRENT_DATE - INTERVAL '30 days' THEN 1 END) as created_last_month
FROM users;

-- Email domain distribution
SELECT 
    SUBSTRING(email FROM '@(.*)$') as email_domain,
    COUNT(*) as user_count
FROM users 
GROUP BY SUBSTRING(email FROM '@(.*)$')
ORDER BY user_count DESC;
