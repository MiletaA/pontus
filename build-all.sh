#!/bin/bash

# Build script for all Pontus microservices
echo "Building Pontus Microservices..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to build a service
build_service() {
    local service_name=$1
    echo -e "${YELLOW}Building $service_name...${NC}"
    
    if [ -d "$service_name" ]; then
        cd "$service_name"
        if mvn clean package -DskipTests; then
            echo -e "${GREEN}✓ $service_name built successfully${NC}"
        else
            echo -e "${RED}✗ Failed to build $service_name${NC}"
            exit 1
        fi
        cd ..
    else
        echo -e "${RED}✗ Directory $service_name not found${NC}"
        exit 1
    fi
}

# Build all services
echo "Starting build process..."

build_service "naming-server"
build_service "auth-service"
build_service "api-gateway"
build_service "vessel-service"
build_service "dock-service"
build_service "cargo-service"
build_service "crew-service"
build_service "delivery-service"

echo -e "${GREEN}All services built successfully!${NC}"
echo "You can now run: docker-compose up -d"
