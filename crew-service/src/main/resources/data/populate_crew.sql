-- Create crew_member table if it doesn't exist
CREATE TABLE IF NOT EXISTS crew_member (
    id BIGSERIAL PRIMARY KEY,
    vessel_id BIGINT,
    name VARCHAR(100) NOT NULL,
    nationality VARCHAR(50) NOT NULL,
    position VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    passport_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clear existing data
DELETE FROM crew_member;

-- Reset sequence
ALTER SEQUENCE crew_member_id_seq RESTART WITH 1;

-- Insert real crew member data representing various maritime positions
-- vessel_id corresponds to vessels from the vessel table
INSERT INTO crew_member (vessel_id, name, nationality, position, date_of_birth, passport_number, created_at, updated_at) VALUES

-- MSC GULSUN CREW (vessel_id 1) - Ultra Large Container Vessel
(1, 'Captain Alessandro Rossi', 'Italian', 'CAPTAIN', '1975-03-15', 'IT7845123A', '2024-01-15 10:00:00', '2024-07-28 09:15:00'),
(1, 'Chief Officer Hans Mueller', 'German', 'DECK_OFFICER', '1982-07-22', 'DE9876543B', '2024-01-15 10:15:00', '2024-07-28 09:30:00'),
(1, 'Second Officer Maria Santos', 'Portuguese', 'DECK_OFFICER', '1988-11-08', 'PT5432167C', '2024-01-15 10:30:00', '2024-07-28 09:45:00'),
(1, 'Third Officer James Wilson', 'British', 'DECK_OFFICER', '1991-05-14', 'GB3456789D', '2024-01-15 10:45:00', '2024-07-28 10:00:00'),
(1, 'Chief Engineer Dimitri Petrov', 'Russian', 'ENGINE_OFFICER', '1978-09-03', 'RU8765432E', '2024-01-15 11:00:00', '2024-07-28 10:15:00'),
(1, 'Second Engineer Lars Andersen', 'Danish', 'ENGINE_OFFICER', '1985-12-19', 'DK2345678F', '2024-01-15 11:15:00', '2024-07-28 10:30:00'),
(1, 'Bosun Carlos Rodriguez', 'Spanish', 'SAILOR', '1980-04-27', 'ES6789012G', '2024-01-15 11:30:00', '2024-07-28 10:45:00'),
(1, 'AB Seaman Raj Patel', 'Indian', 'SAILOR', '1992-08-11', 'IN4567890H', '2024-01-15 11:45:00', '2024-07-28 11:00:00'),
(1, 'AB Seaman Chen Wei', 'Chinese', 'SAILOR', '1989-02-28', 'CN7890123I', '2024-01-15 12:00:00', '2024-07-28 11:15:00'),
(1, 'Cook Ahmed Hassan', 'Egyptian', 'COOK', '1987-06-16', 'EG1234567J', '2024-01-15 12:15:00', '2024-07-28 11:30:00'),

-- EVER GIVEN CREW (vessel_id 2) - Ultra Large Container Vessel
(2, 'Captain Hiroshi Tanaka', 'Japanese', 'CAPTAIN', '1973-01-20', 'JP9876543K', '2024-01-16 08:00:00', '2024-08-02 08:00:00'),
(2, 'Chief Officer Park Min-jun', 'South Korean', 'DECK_OFFICER', '1981-10-05', 'KR5432167L', '2024-01-16 08:15:00', '2024-08-02 08:15:00'),
(2, 'Second Officer Liu Ming', 'Chinese', 'DECK_OFFICER', '1986-12-12', 'CN3456789M', '2024-01-16 08:30:00', '2024-08-02 08:30:00'),
(2, 'Chief Engineer Vladimir Kozlov', 'Ukrainian', 'ENGINE_OFFICER', '1976-07-30', 'UA8765432N', '2024-01-16 08:45:00', '2024-08-02 08:45:00'),
(2, 'Radio Officer Nikos Papadopoulos', 'Greek', 'RADIO_OPERATOR', '1984-03-18', 'GR2345678O', '2024-01-16 09:00:00', '2024-08-02 09:00:00'),
(2, 'Bosun Fernando Silva', 'Brazilian', 'SAILOR', '1979-11-25', 'BR6789012P', '2024-01-16 09:15:00', '2024-08-02 09:15:00'),
(2, 'AB Seaman Olaf Larsen', 'Norwegian', 'SAILOR', '1990-09-07', 'NO4567890Q', '2024-01-16 09:30:00', '2024-08-02 09:30:00'),
(2, 'Oiler Ravi Kumar', 'Indian', 'ENGINEER', '1988-05-22', 'IN7890123R', '2024-01-16 09:45:00', '2024-08-02 09:45:00'),

-- HMM ALGECIRAS CREW (vessel_id 4) - Ultra Large Container Vessel
(4, 'Captain Kim Jong-su', 'South Korean', 'CAPTAIN', '1972-08-14', 'KR1234567S', '2024-01-17 10:00:00', '2024-07-30 13:30:00'),
(4, 'Chief Officer Lee Sung-min', 'South Korean', 'DECK_OFFICER', '1983-04-09', 'KR9876543T', '2024-01-17 10:15:00', '2024-07-30 13:45:00'),
(4, 'Chief Engineer Park Hyun-woo', 'South Korean', 'ENGINE_OFFICER', '1977-12-03', 'KR5432167U', '2024-01-17 10:30:00', '2024-07-30 14:00:00'),
(4, 'Second Officer Zhang Yifei', 'Chinese', 'DECK_OFFICER', '1987-06-28', 'CN3456789V', '2024-01-17 10:45:00', '2024-07-30 14:15:00'),
(4, 'Bosun Miguel Santos', 'Filipino', 'SAILOR', '1981-01-17', 'PH8765432W', '2024-01-17 11:00:00', '2024-07-30 14:30:00'),

-- TI EUROPE CREW (vessel_id 9) - Supertanker
(9, 'Captain Erik Johansson', 'Swedish', 'CAPTAIN', '1974-05-11', 'SE2345678X', '2024-01-18 09:00:00', '2024-07-29 17:30:00'),
(9, 'Chief Officer Antonio Fernandez', 'Spanish', 'DECK_OFFICER', '1980-09-26', 'ES6789012Y', '2024-01-18 09:15:00', '2024-07-29 17:45:00'),
(9, 'Chief Engineer Sergei Volkov', 'Russian', 'ENGINE_OFFICER', '1976-02-14', 'RU4567890Z', '2024-01-18 09:30:00', '2024-07-29 18:00:00'),
(9, 'Pumpman Abdul Rahman', 'Pakistani', 'ENGINEER', '1985-11-02', 'PK7890123A1', '2024-01-18 09:45:00', '2024-07-29 18:15:00'),
(9, 'AB Seaman Tomasz Kowalski', 'Polish', 'SAILOR', '1989-07-19', 'PL1234567B1', '2024-01-18 10:00:00', '2024-07-29 18:30:00'),

-- VALE BRASIL CREW (vessel_id 13) - Bulk Carrier
(13, 'Captain João Silva', 'Brazilian', 'CAPTAIN', '1971-12-08', 'BR9876543C1', '2024-01-19 08:00:00', '2024-07-27 15:20:00'),
(13, 'Chief Officer Ricardo Santos', 'Brazilian', 'DECK_OFFICER', '1982-03-25', 'BR5432167D1', '2024-01-19 08:15:00', '2024-07-27 15:35:00'),
(13, 'Chief Engineer Mikhail Petrov', 'Russian', 'ENGINE_OFFICER', '1978-10-12', 'RU3456789E1', '2024-01-19 08:30:00', '2024-07-27 15:50:00'),
(13, 'Second Officer Carlos Mendoza', 'Mexican', 'DECK_OFFICER', '1986-01-30', 'MX8765432F1', '2024-01-19 08:45:00', '2024-07-27 16:05:00'),
(13, 'Bosun Luiz Oliveira', 'Brazilian', 'SAILOR', '1983-08-17', 'BR2345678G1', '2024-01-19 09:00:00', '2024-07-27 16:20:00'),

-- SYMPHONY OF THE SEAS CREW (vessel_id 18) - Cruise Ship
(18, 'Captain Francesco Moretti', 'Italian', 'CAPTAIN', '1970-04-22', 'IT6789012H1', '2024-01-20 07:00:00', '2024-07-28 07:15:00'),
(18, 'Staff Captain Sarah Johnson', 'American', 'CAPTAIN', '1979-11-15', 'US4567890I1', '2024-01-20 07:15:00', '2024-07-28 07:30:00'),
(18, 'Chief Officer Pierre Dubois', 'French', 'DECK_OFFICER', '1981-06-03', 'FR7890123J1', '2024-01-20 07:30:00', '2024-07-28 07:45:00'),
(18, 'Hotel Director Klaus Weber', 'German', 'DECK_OFFICER', '1975-09-28', 'DE1234567K1', '2024-01-20 07:45:00', '2024-07-28 08:00:00'),
(18, 'Chief Engineer Yuki Yamamoto', 'Japanese', 'ENGINE_OFFICER', '1977-02-11', 'JP9876543L1', '2024-01-20 08:00:00', '2024-07-28 08:15:00'),
(18, 'Food & Beverage Manager Maria Gonzalez', 'Spanish', 'COOK', '1984-12-07', 'ES5432167M1', '2024-01-20 08:15:00', '2024-07-28 08:30:00'),
(18, 'Entertainment Director David Smith', 'British', 'DECK_OFFICER', '1982-05-19', 'GB3456789N1', '2024-01-20 08:30:00', '2024-07-28 08:45:00'),
(18, 'Security Officer Michael Brown', 'Canadian', 'SAILOR', '1985-08-04', 'CA8765432O1', '2024-01-20 08:45:00', '2024-07-28 09:00:00'),

-- MADRID MAERSK CREW (vessel_id 8) - Container Vessel
(8, 'Captain Niels Christensen', 'Danish', 'CAPTAIN', '1973-07-16', 'DK2345678P1', '2024-01-21 09:00:00', '2024-07-29 17:30:00'),
(8, 'Chief Officer Bjorn Larsen', 'Norwegian', 'DECK_OFFICER', '1980-01-23', 'NO6789012Q1', '2024-01-21 09:15:00', '2024-07-29 17:45:00'),
(8, 'Chief Engineer Sven Andersson', 'Swedish', 'ENGINE_OFFICER', '1976-11-09', 'SE4567890R1', '2024-01-21 09:30:00', '2024-07-29 18:00:00'),
(8, 'Second Officer Finn Virtanen', 'Finnish', 'DECK_OFFICER', '1987-04-14', 'FI7890123S1', '2024-01-21 09:45:00', '2024-07-29 18:15:00'),

-- VALEMAX PIONEER CREW (vessel_id 17) - Bulk Carrier
(17, 'Captain Roberto Almeida', 'Brazilian', 'CAPTAIN', '1972-10-05', 'BR1234567T1', '2024-01-22 08:00:00', '2024-08-01 07:30:00'),
(17, 'Chief Officer Paulo Costa', 'Brazilian', 'DECK_OFFICER', '1983-02-18', 'BR9876543U1', '2024-01-22 08:15:00', '2024-08-01 07:45:00'),
(17, 'Chief Engineer Igor Volkov', 'Russian', 'ENGINE_OFFICER', '1977-06-29', 'RU5432167V1', '2024-01-22 08:30:00', '2024-08-01 08:00:00'),
(17, 'Second Officer Chen Li', 'Chinese', 'DECK_OFFICER', '1988-12-13', 'CN3456789W1', '2024-01-22 08:45:00', '2024-08-01 08:15:00'),

-- WONDER OF THE SEAS CREW (vessel_id 21) - Cruise Ship
(21, 'Captain Jean-Luc Moreau', 'French', 'CAPTAIN', '1969-03-12', 'FR8765432X1', '2024-01-23 07:00:00', '2024-08-01 09:30:00'),
(21, 'Staff Captain Emma Thompson', 'British', 'CAPTAIN', '1978-08-27', 'GB2345678Y1', '2024-01-23 07:15:00', '2024-08-01 09:45:00'),
(21, 'Hotel Director Giuseppe Romano', 'Italian', 'DECK_OFFICER', '1974-01-08', 'IT6789012Z1', '2024-01-23 07:30:00', '2024-08-01 10:00:00'),
(21, 'Chief Engineer Akira Suzuki', 'Japanese', 'ENGINE_OFFICER', '1976-05-21', 'JP4567890A2', '2024-01-23 07:45:00', '2024-08-01 10:15:00'),

-- MAERSK ALABAMA CREW (vessel_id 23) - General Cargo
(23, 'Captain Richard Phillips', 'American', 'CAPTAIN', '1955-05-16', 'US7890123B2', '2024-01-24 10:00:00', '2024-07-30 15:30:00'),
(23, 'Chief Officer Shane Murphy', 'American', 'DECK_OFFICER', '1970-09-02', 'US1234567C2', '2024-01-24 10:15:00', '2024-07-30 15:45:00'),
(23, 'Chief Engineer Mike Perry', 'American', 'ENGINE_OFFICER', '1968-12-14', 'US9876543D2', '2024-01-24 10:30:00', '2024-07-30 16:00:00'),
(23, 'AB Seaman ATM Reza', 'Bangladeshi', 'SAILOR', '1985-07-08', 'BD5432167E2', '2024-01-24 10:45:00', '2024-07-30 16:15:00'),

-- Additional crew members for other vessels
(5, 'Captain Marco Bianchi', 'Italian', 'CAPTAIN', '1974-11-30', 'IT3456789F2', '2024-01-25 09:00:00', '2024-07-25 11:20:00'),
(5, 'Chief Officer Luca Rossi', 'Italian', 'DECK_OFFICER', '1981-04-17', 'IT8765432G2', '2024-01-25 09:15:00', '2024-07-25 11:35:00'),
(6, 'Captain Henri Leclerc', 'French', 'CAPTAIN', '1975-08-23', 'FR2345678H2', '2024-01-26 08:00:00', '2024-08-06 10:00:00'),
(7, 'Captain Wang Xiaoming', 'Chinese', 'CAPTAIN', '1973-03-06', 'CN6789012I2', '2024-01-27 08:00:00', '2024-08-03 12:00:00'),

-- Additional crew members assigned to other vessels
(10, 'Captain Thomas Anderson', 'Australian', 'CAPTAIN', '1971-06-19', 'AU4567890J2', '2024-01-28 10:00:00', '2024-07-15 14:30:00'),
(11, 'Chief Officer Maria Petrova', 'Bulgarian', 'DECK_OFFICER', '1982-10-11', 'BG7890123K2', '2024-01-28 10:15:00', '2024-07-20 16:45:00'),
(12, 'Chief Engineer Hassan Al-Rashid', 'UAE', 'ENGINE_OFFICER', '1979-01-25', 'AE1234567L2', '2024-01-28 10:30:00', '2024-07-25 12:20:00'),
(13, 'Second Officer Anna Kowalski', 'Polish', 'DECK_OFFICER', '1986-09-13', 'PL9876543M2', '2024-01-28 10:45:00', '2024-07-30 18:15:00'),
(14, 'Bosun Dimitrios Stavros', 'Greek', 'SAILOR', '1984-05-07', 'GR5432167N2', '2024-01-28 11:00:00', '2024-08-01 11:30:00');

-- Verification queries
SELECT 
    position,
    COUNT(*) as crew_count
FROM crew_member 
GROUP BY position
ORDER BY crew_count DESC;

SELECT 
    nationality,
    COUNT(*) as crew_count
FROM crew_member 
GROUP BY nationality
ORDER BY crew_count DESC;

SELECT 
    CASE 
        WHEN vessel_id IS NULL THEN 'Unassigned'
        ELSE 'Assigned'
    END as assignment_status,
    COUNT(*) as crew_count
FROM crew_member 
GROUP BY vessel_id IS NULL;
