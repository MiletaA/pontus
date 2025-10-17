#!/bin/bash

# Docker Hub Build and Push Script for Pontus Microservices
# Usage: ./build-and-push-dockerhub.sh [your-dockerhub-username]

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Docker Hub username is provided
if [ -z "$1" ]; then
    echo -e "${RED}Error: Please provide your Docker Hub username${NC}"
    echo "Usage: $0 [your-dockerhub-username]"
    exit 1
fi

DOCKERHUB_USERNAME=$1
VERSION=${2:-latest}

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Pontus Docker Hub Build & Push       ${NC}"
echo -e "${BLUE}========================================${NC}"
echo -e "${YELLOW}Docker Hub Username: $DOCKERHUB_USERNAME${NC}"
echo -e "${YELLOW}Version Tag: $VERSION${NC}"
echo ""

# Function to build and push a service
build_and_push_service() {
    local service_name=$1
    local image_name="$DOCKERHUB_USERNAME/pontus-$service_name:$VERSION"
    
    echo -e "${YELLOW}Building and pushing $service_name...${NC}"
    
    if [ -d "$service_name" ]; then
        cd "$service_name"
        
        # Build the Docker image
        echo -e "${BLUE}Building Docker image: $image_name${NC}"
        if docker build -t "$image_name" .; then
            echo -e "${GREEN}✓ Image built successfully${NC}"
        else
            echo -e "${RED}✗ Failed to build $service_name${NC}"
            exit 1
        fi
        
        # Push to Docker Hub
        echo -e "${BLUE}Pushing to Docker Hub: $image_name${NC}"
        if docker push "$image_name"; then
            echo -e "${GREEN}✓ $service_name pushed successfully${NC}"
        else
            echo -e "${RED}✗ Failed to push $service_name${NC}"
            exit 1
        fi
        
        cd ..
        echo ""
    else
        echo -e "${RED}✗ Directory $service_name not found${NC}"
        exit 1
    fi
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running. Please start Docker first.${NC}"
    exit 1
fi

# Check if user is logged into Docker Hub
if ! docker info | grep -q "Username:"; then
    echo -e "${YELLOW}Please log in to Docker Hub first:${NC}"
    echo "docker login"
    exit 1
fi

echo -e "${YELLOW}Starting Docker Hub build and push process...${NC}"
echo -e "${BLUE}Note: Each service will be compiled inside its Docker container${NC}"
echo ""

# Build and push all services
build_and_push_service "naming-server"
build_and_push_service "auth-service"
build_and_push_service "api-gateway"
build_and_push_service "vessel-service"
build_and_push_service "dock-service"
build_and_push_service "cargo-service"
build_and_push_service "crew-service"
build_and_push_service "delivery-service"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  All services pushed successfully!    ${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Update docker-compose.production.yml with your Docker Hub username"
echo "2. Share the docker-compose.production.yml with users"
echo "3. Users can run: docker-compose -f docker-compose.production.yml up -d"
echo ""
echo -e "${BLUE}Docker images pushed:${NC}"
echo "- $DOCKERHUB_USERNAME/pontus-naming-server:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-auth-service:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-api-gateway:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-vessel-service:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-dock-service:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-cargo-service:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-crew-service:$VERSION"
echo "- $DOCKERHUB_USERNAME/pontus-delivery-service:$VERSION"
