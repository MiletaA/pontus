/**
 * Dock model interfaces matching the backend API
 */

export interface Dock {
  id: number;
  name: string;
  maxLength: number;
  isOccupied: boolean;
  assignedVesselId?: number;
  scheduledFrom?: string;
  scheduledTo?: string;
  handlesDangerous: boolean;
  description?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface DockCreateRequest {
  name: string;
  maxLength: number;
  handlesDangerous: boolean;
  description?: string;
}

export interface DockUpdateRequest {
  name?: string;
  maxLength?: number;
  handlesDangerous?: boolean;
  description?: string;
}

export interface DockAssignmentRequest {
  vesselId: number;
  scheduledFrom: string;
  scheduledTo: string;
}

export interface DockFilters {
  isOccupied?: boolean;
  handlesDangerous?: boolean;
  search?: string;
  minLength?: number;
  maxLength?: number;
}

export interface DockStatusInfo {
  isOccupied: boolean;
  label: string;
  description: string;
  color: string;
  icon: string;
}

export const DOCK_STATUS_INFO: Record<string, DockStatusInfo> = {
  'occupied': {
    isOccupied: true,
    label: 'Occupied',
    description: 'Dock is currently assigned to a vessel',
    color: 'danger',
    icon: 'ship'
  },
  'available': {
    isOccupied: false,
    label: 'Available',
    description: 'Dock is free and ready for assignment',
    color: 'success',
    icon: 'check-circle'
  }
};
