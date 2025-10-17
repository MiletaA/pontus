/**
 * Authentication model interfaces matching the backend API
 */

export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  enabled: boolean;
  createdAt: string;
  updatedAt?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: Role;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  type: string;
  username: string;
  email: string;
  role: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export enum Role {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  HARBOR_MASTER = 'HARBOR_MASTER',
  CUSTOMS_OFFICER = 'CUSTOMS_OFFICER',
  DOCK_WORKER = 'DOCK_WORKER',
  VESSEL_CAPTAIN = 'VESSEL_CAPTAIN',
  OPERATIONS = 'OPERATIONS',
  USER = 'USER'
}

export const ROLES = Object.values(Role);

export interface RoleInfo {
  role: Role;
  label: string;
  description: string;
  color: string;
  permissions: string[];
}

export const ROLE_INFO: Record<Role, RoleInfo> = {
  [Role.ADMIN]: {
    role: Role.ADMIN,
    label: 'Administrator',
    description: 'Full system access and user management',
    color: 'danger',
    permissions: ['CREATE', 'READ', 'UPDATE', 'DELETE', 'MANAGE_USERS']
  },
  [Role.MANAGER]: {
    role: Role.MANAGER,
    label: 'Port Manager',
    description: 'Manages port operations and strategic decisions',
    color: 'primary',
    permissions: ['CREATE', 'READ', 'UPDATE', 'DELETE']
  },
  [Role.HARBOR_MASTER]: {
    role: Role.HARBOR_MASTER,
    label: 'Harbor Master',
    description: 'Controls vessel traffic and port safety',
    color: 'warning',
    permissions: ['CREATE', 'READ', 'UPDATE', 'DELETE']
  },
  [Role.CUSTOMS_OFFICER]: {
    role: Role.CUSTOMS_OFFICER,
    label: 'Customs Officer',
    description: 'Handles customs clearance and cargo inspection',
    color: 'success',
    permissions: ['READ', 'UPDATE']
  },
  [Role.DOCK_WORKER]: {
    role: Role.DOCK_WORKER,
    label: 'Dock Worker',
    description: 'Handles cargo operations and dock activities',
    color: 'secondary',
    permissions: ['READ', 'UPDATE']
  },
  [Role.VESSEL_CAPTAIN]: {
    role: Role.VESSEL_CAPTAIN,
    label: 'Vessel Captain',
    description: 'Commands vessels and crew operations',
    color: 'dark',
    permissions: ['READ', 'UPDATE']
  },
  [Role.OPERATIONS]: {
    role: Role.OPERATIONS,
    label: 'Operations Coordinator',
    description: 'Coordinates daily port operations',
    color: 'info',
    permissions: ['CREATE', 'READ', 'UPDATE']
  },
  [Role.USER]: {
    role: Role.USER,
    label: 'User',
    description: 'Basic access to port information',
    color: 'light',
    permissions: ['READ']
  }
};
