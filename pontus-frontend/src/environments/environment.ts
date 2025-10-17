export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  auth: {
    baseUrl: 'http://localhost:8080/auth'
  },
  services: {
    vessels: 'http://localhost:8080/vessels',
    cargo: 'http://localhost:8080/cargo',
    crew: 'http://localhost:8080/crew',
    docks: 'http://localhost:8080/docks',
    deliveries: 'http://localhost:8080/deliveries'
  }
};
