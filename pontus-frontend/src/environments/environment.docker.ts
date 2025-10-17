export const environment = {
  production: false,
  apiUrl: 'http://api-gateway:8080',
  auth: {
    baseUrl: 'http://api-gateway:8080/auth'
  },
  services: {
    vessels: 'http://api-gateway:8080/vessels',
    cargo: 'http://api-gateway:8080/cargo',
    crew: 'http://api-gateway:8080/crew',
    docks: 'http://api-gateway:8080/docks',
    deliveries: 'http://api-gateway:8080/deliveries'
  }
};
