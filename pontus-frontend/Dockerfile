# Multi-stage build for Angular frontend
FROM node:18-alpine as build

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Build the Angular app for Docker environment
RUN npm run build -- --configuration=docker

# Production stage with nginx
FROM nginx:alpine

# Copy built app to nginx
COPY --from=build /app/dist/pontus-frontend /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 4200 (matching Angular dev server)
EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
