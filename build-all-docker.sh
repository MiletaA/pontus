#!/bin/bash

# Build all services with Docker
echo "Building all Pontus services with Docker..."

# Build each service
echo "Building dock-service..."
docker build -t pontus-dock-service:local ./dock-service

echo "Building vessel-service..."
docker build -t pontus-vessel-service:local ./vessel-service

echo "Building cargo-service..."
docker build -t pontus-cargo-service:local ./cargo-service

echo "Building crew-service..."
docker build -t pontus-crew-service:local ./crew-service

echo "Building delivery-service..."
docker build -t pontus-delivery-service:local ./delivery-service

echo "Building naming-server (if needed)..."
docker build -t pontus-naming-server:local ./naming-server 2>/dev/null || echo "Using existing naming-server image"

echo "All services built successfully!"
