# 🚢 Pontus Harbor Management System

A comprehensive microservices-based harbor management platform for coordinating vessel operations, crew management, cargo tracking, dock allocation, and inland deliveries.

[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Database Structure](#-database-structure)
- [Microservices](#-microservices)
- [Getting Started](#-getting-started)
- [Future Enhancements](#-future-enhancements)

---

## 🌊 Overview

Pontus is a modern, cloud-native harbor management system designed to digitize and streamline port operations. Built using microservices architecture, it provides:

✅ **Real-time vessel tracking** - Monitor arrivals, departures, and operational status  
✅ **Crew management** - Track certifications, qualifications, and vessel assignments  
✅ **Cargo operations** - Manage inventory, customs clearance, and dangerous goods  
✅ **Dock allocation** - Optimize berth assignments and capacity planning  
✅ **Delivery coordination** - Track inland transportation from port to destination  
✅ **Role-based security** - JWT authentication with fine-grained access control  

---

## 🗄️ Database Structure

Each microservice maintains its own dedicated PostgreSQL database following the Database-per-Service pattern.

### 1. Users Table (Auth Database - Port 5437)

**Table Name:** `users`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | Unique user identifier |
| `username` | VARCHAR(50) | NOT NULL, UNIQUE | User login name |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE | User email address |
| `password` | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| `first_name` | VARCHAR(50) | NOT NULL | User first name |
| `last_name` | VARCHAR(50) | NOT NULL | User last name |
| `role` | VARCHAR(50) | NOT NULL | User role (ENUM) |
| `enabled` | BOOLEAN | NOT NULL, DEFAULT TRUE | Account active status |
| `created_at` | TIMESTAMP | NOT NULL | Account creation time |
| `updated_at` | TIMESTAMP | | Last update time |

**Valid Roles:** `ADMIN`, `MANAGER`, `HARBOR_MASTER`, `CUSTOMS_OFFICER`, `VESSEL_CAPTAIN`, `DOCK_WORKER`, `OPERATIONS`, `USER`

**Indexes:** `idx_users_username`, `idx_users_email`, `idx_users_role`

---

### 2. Vessel Table (Vessel Database - Port 5431)

**Table Name:** `vessel`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | Unique vessel identifier |
| `name` | VARCHAR(100) | NOT NULL | Vessel name |
| `imo_number` | VARCHAR(20) | NOT NULL, UNIQUE | International Maritime Organization number (IMO + 7 digits) |
| `vessel_type` | VARCHAR(50) | NOT NULL | Type of vessel (ENUM) |
| `length` | DECIMAL(10,2) | NOT NULL, >0 | Vessel length in meters |
| `flag_country` | VARCHAR(50) | NOT NULL | Flag state/country of registration |
| `status` | VARCHAR(50) | NOT NULL | Current operational status (ENUM) |
| `scheduled_arrival` | TIMESTAMP | | Expected arrival time |
| `scheduled_departure` | TIMESTAMP | | Expected departure time |
| `actual_arrival` | TIMESTAMP | | Actual arrival time |
| `actual_departure` | TIMESTAMP | | Actual departure time |

**Vessel Types:** `CONTAINER_SHIP`, `BULK_CARRIER`, `TANKER`, `GENERAL_CARGO`, `RO_RO`, `LNG_CARRIER`, `CRUISE_SHIP`, `CARGO_SHIP`, `SUPPLY_VESSEL`, `OTHER`

**Vessel Status:** `SCHEDULED`, `UNDERWAY`, `ANCHORED`, `BERTHED`, `DEPARTED`

**Indexes:** `idx_vessel_imo`, `idx_vessel_status`, `idx_vessel_type`

**Constraints:**
- IMO number must match pattern `^IMO[0-9]{7}$`
- Scheduled departure cannot be before scheduled arrival
- Actual departure cannot be before actual arrival

---

### 3. Crew Member Table (Crew Database - Port 5435)

**Table Name:** `crew_member`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | Unique crew member identifier |
| `vessel_id` | BIGINT | NOT NULL | Reference to assigned vessel |
| `name` | VARCHAR(100) | NOT NULL | Crew member full name |
| `nationality` | VARCHAR(50) | NOT NULL | Nationality/citizenship |
| `position` | VARCHAR(50) | NOT NULL | Maritime position/rank (ENUM) |
| `date_of_birth` | DATE | NOT NULL | Date of birth (must be in past) |
| `passport_number` | VARCHAR(50) | NOT NULL | Passport identification number |
| `certificate` | VARCHAR(50) | | Maritime certification type |
| `certificate_expiry` | DATE | | Certification expiry date |
| `created_at` | TIMESTAMP | NOT NULL | Record creation time |
| `updated_at` | TIMESTAMP | | Last update time |

**Positions:** `CAPTAIN`, `ENGINEER`, `SAILOR`, `COOK`, `RADIO_OPERATOR`, `DECK_OFFICER`, `ENGINE_OFFICER`

**Indexes:** `idx_crew_vessel`, `idx_crew_position`, `idx_crew_nationality`

---

### 4. Cargo Table (Cargo Database - Port 5434)

**Table Name:** `cargo`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | Unique cargo identifier |
| `vessel_id` | BIGINT | NOT NULL | Reference to carrying vessel |
| `description` | VARCHAR(1000) | NOT NULL | Detailed cargo description |
| `weight_tons` | DECIMAL(10,2) | NOT NULL, >0 | Weight in metric tons |
| `is_dangerous` | BOOLEAN | NOT NULL, DEFAULT FALSE | Dangerous goods flag |
| `customs_status` | VARCHAR(50) | NOT NULL | Customs clearance status (ENUM) |
| `cargo_type` | VARCHAR(50) | | Type of cargo (ENUM) |
| `origin` | VARCHAR(100) | | Port or location of origin |
| `destination` | VARCHAR(100) | | Destination port or location |
| `created_at` | TIMESTAMP | NOT NULL | Record creation time |
| `updated_at` | TIMESTAMP | | Last update time |

**Customs Status:** `PENDING`, `CLEARED`, `INSPECTION_REQUIRED`, `HELD`, `REJECTED`

**Cargo Types:** `CONTAINER`, `BULK_DRY`, `BULK_LIQUID`, `BREAKBULK`, `VEHICLES`, `DANGEROUS_GOODS`, `REFRIGERATED`, `LIVESTOCK`, `PROJECT_CARGO`, `GENERAL`

**Indexes:** `idx_cargo_vessel`, `idx_cargo_customs`, `idx_cargo_type`, `idx_cargo_dangerous`

---

### 5. Dock Table (Dock Database - Port 5433)

**Table Name:** `dock`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | Unique dock identifier |
| `name` | VARCHAR(100) | NOT NULL | Dock name/designation |
| `max_length` | DECIMAL(10,2) | NOT NULL, >0 | Maximum vessel length in meters |
| `is_occupied` | BOOLEAN | NOT NULL, DEFAULT FALSE | Current occupation status |
| `assigned_vessel_id` | BIGINT | | Currently assigned vessel (if any) |
| `scheduled_from` | TIMESTAMP | | Assignment start time |
| `scheduled_to` | TIMESTAMP | | Assignment end time |
| `handles_dangerous` | BOOLEAN | NOT NULL, DEFAULT FALSE | Can handle dangerous cargo |
| `description` | VARCHAR(500) | | Additional dock information |
| `created_at` | TIMESTAMP | NOT NULL | Record creation time |
| `updated_at` | TIMESTAMP | | Last update time |

**Indexes:** `idx_dock_occupied`, `idx_dock_vessel`, `idx_dock_dangerous`

---

### 6. Inland Delivery Table (Delivery Database - Port 5436)

**Table Name:** `inland_delivery`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | BIGSERIAL | PRIMARY KEY | Unique delivery identifier |
| `cargo_id` | BIGINT | NOT NULL | Reference to cargo being delivered |
| `destination_address` | VARCHAR(500) | NOT NULL | Full delivery address |
| `delivery_status` | VARCHAR(50) | NOT NULL | Current delivery status (ENUM) |
| `vehicle_registration` | VARCHAR(20) | NOT NULL | Delivery vehicle registration |
| `driver_name` | VARCHAR(100) | NOT NULL | Assigned driver name |
| `delivery_time` | TIMESTAMP | | Actual delivery completion time |
| `scheduled_delivery_time` | TIMESTAMP | | Scheduled delivery time |
| `notes` | VARCHAR(500) | | Additional delivery notes |
| `created_at` | TIMESTAMP | NOT NULL | Record creation time |
| `updated_at` | TIMESTAMP | | Last update time |

**Delivery Status:** `PENDING`, `IN_TRANSIT`, `DELIVERED`, `FAILED`, `CANCELLED`

**Indexes:** `idx_delivery_cargo`, `idx_delivery_status`, `idx_delivery_scheduled`

---


## 🔧 Microservices

### Service Registry

| Service | Port | Database | Docker Image | Purpose |
|---------|------|----------|--------------|---------|
| **Naming Server** | 8761 | - | `miletaa/pontus-naming-server` | Eureka service discovery and registration |
| **API Gateway** | 8080 | - | `miletaa/pontus-api-gateway` | Single entry point, routing, and load balancing |
| **Auth Service** | 8086 | 5437 | `miletaa/pontus-auth-service` | User authentication and authorization |
| **Vessel Service** | 8081 | 5431 | `miletaa/pontus-vessel-service` | Vessel information and tracking |
| **Crew Service** | 8084 | 5435 | `miletaa/pontus-crew-service` | Crew member management |
| **Cargo Service** | 8083 | 5434 | `miletaa/pontus-cargo-service` | Cargo tracking and customs |
| **Dock Service** | 8082 | 5433 | `miletaa/pontus-dock-service` | Dock allocation and management |
| **Delivery Service** | 8085 | 5436 | `miletaa/pontus-delivery-service` | Inland delivery coordination |

### Key Features by Service

#### Auth Service (Port 8086)
- JWT token generation and validation
- BCrypt password encryption
- Role-based access control
- User registration and login
- Token refresh mechanism

#### Vessel Service (Port 8081)
- Vessel registration and tracking
- IMO number validation
- Status updates (arrival, departure, berthing)
- Vessel type and specifications management
- Scheduled vs actual time tracking

#### Crew Service (Port 8084)
- Crew member registration
- Position and rank management
- Certificate tracking and expiry monitoring
- Vessel assignment validation
- Passport and identification management

#### Cargo Service (Port 8083)
- Cargo registration and tracking
- Customs status management
- Dangerous goods handling
- Weight and type classification
- Origin and destination tracking

#### Dock Service (Port 8082)
- Dock availability tracking
- Vessel-dock assignment
- Capacity management
- Dangerous cargo handling capability
- Scheduling window management

#### Delivery Service (Port 8085)
- Delivery scheduling
- Status tracking (pending, in-transit, delivered)
- Vehicle and driver assignment
- Delivery confirmation
- Route planning support

---


### Access Points

- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Auth Service**: http://localhost:8086
- **Vessel Service**: http://localhost:8081
- **Individual Services**: http://localhost:{service_port}

---

## 🔮 Future Enhancements

### Planned Features

#### 1. Spring Boot Actuator Integration
- **Health Checks**: Detailed service health status
- **Metrics**: Performance and resource monitoring
- **Tracing**: Distributed request tracing
- **Audit Events**: Security and operational auditing
- **Custom Endpoints**: Business-specific monitoring

**Planned Endpoints:**
- `/actuator/health` - Service health status
- `/actuator/metrics` - Performance metrics
- `/actuator/prometheus` - Prometheus-compatible metrics
- `/actuator/info` - Application information
- `/actuator/loggers` - Log level management

#### 2. Frontend Application
- **Technology Stack**: React/Angular with TypeScript
- **Features**:
  - Real-time dashboard for harbor operations
  - Vessel tracking with live status updates
  - Interactive dock allocation visualization
  - Cargo and customs management interface
  - User management and role assignment
  - Responsive mobile-friendly design
  - WebSocket support for live updates

**Planned Modules:**
- Harbor Overview Dashboard
- Vessel Management Interface
- Crew Assignment Portal
- Cargo Tracking System
- Dock Allocation Manager
- Delivery Coordination Panel
- User Administration Console
- Reports and Analytics

#### 3. Additional Enhancements
- **Monitoring**: Grafana dashboards for system metrics
- **Logging**: Centralized logging with ELK stack
- **API Documentation**: Swagger/OpenAPI integration
- **Testing**: Comprehensive integration test suite
- **CI/CD**: Automated build and deployment pipelines
- **Notifications**: Email/SMS alerts for critical events
- **Mobile App**: Native iOS/Android applications
