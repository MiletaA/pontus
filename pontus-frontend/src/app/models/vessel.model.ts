/**
 * Vessel model interfaces matching the backend API
 */

export interface Vessel {
  id: number;
  name: string;
  imoNumber: string;
  vesselType: string;
  length: number;
  flagCountry: string;
  status: VesselStatus;
  scheduledArrival?: string;
  scheduledDeparture?: string;
  actualArrival?: string;
  actualDeparture?: string;
}

export interface VesselCreateRequest {
  name: string;
  imoNumber: string;
  vesselType: string;
  length: number;
  flagCountry: string;
  status: VesselStatus;
  scheduledArrival?: string;
  scheduledDeparture?: string;
  actualArrival?: string;
  actualDeparture?: string;
}

export interface VesselUpdateRequest {
  name?: string;
  imoNumber?: string;
  vesselType?: string;
  length?: number;
  flagCountry?: string;
  status?: VesselStatus;
  scheduledArrival?: string;
  scheduledDeparture?: string;
  actualArrival?: string;
  actualDeparture?: string;
}

// Vessel Status Enum
export enum VesselStatus {
  SCHEDULED = 'SCHEDULED',
  UNDERWAY = 'UNDERWAY',
  ANCHORED = 'ANCHORED',
  BERTHED = 'BERTHED',
  DEPARTED = 'DEPARTED',
  DELAYED = 'DELAYED',
  CANCELLED = 'CANCELLED'
}

// Vessel Type Enum
export enum VesselType {
  CONTAINER_SHIP = 'CONTAINER_SHIP',
  BULK_CARRIER = 'BULK_CARRIER',
  TANKER = 'TANKER',
  GENERAL_CARGO = 'GENERAL_CARGO',
  RO_RO = 'RO_RO',
  PASSENGER = 'PASSENGER',
  CRUISE = 'CRUISE',
  FERRY = 'FERRY',
  FISHING = 'FISHING',
  OFFSHORE = 'OFFSHORE',
  TUGBOAT = 'TUGBOAT',
  PILOT = 'PILOT',
  RESEARCH = 'RESEARCH',
  MILITARY = 'MILITARY',
  YACHT = 'YACHT',
  OTHER = 'OTHER'
}

// Export constants for runtime access
export const VESSEL_STATUSES = Object.values(VesselStatus);
export const VESSEL_TYPES = Object.values(VesselType);

export interface VesselStatusInfo {
  status: VesselStatus;
  label: string;
  description: string;
  color: string;
  icon: string;
}

export const VESSEL_STATUS_INFO: Record<VesselStatus, VesselStatusInfo> = {
  [VesselStatus.SCHEDULED]: {
    status: VesselStatus.SCHEDULED,
    label: 'Scheduled',
    description: 'Vessel visit is planned and scheduled',
    color: 'secondary',
    icon: 'calendar-alt'
  },
  [VesselStatus.UNDERWAY]: {
    status: VesselStatus.UNDERWAY,
    label: 'Underway',
    description: 'Vessel is en route to the port',
    color: 'info',
    icon: 'route'
  },
  [VesselStatus.ANCHORED]: {
    status: VesselStatus.ANCHORED,
    label: 'Anchored',
    description: 'Vessel is waiting in anchorage area',
    color: 'warning',
    icon: 'anchor'
  },
  [VesselStatus.BERTHED]: {
    status: VesselStatus.BERTHED,
    label: 'Berthed',
    description: 'Vessel is docked at a specific berth/dock',
    color: 'success',
    icon: 'ship'
  },
  [VesselStatus.DEPARTED]: {
    status: VesselStatus.DEPARTED,
    label: 'Departed',
    description: 'Vessel has left the port',
    color: 'dark',
    icon: 'sign-out-alt'
  },
  [VesselStatus.DELAYED]: {
    status: VesselStatus.DELAYED,
    label: 'Delayed',
    description: 'Vessel schedule is delayed',
    color: 'warning',
    icon: 'clock'
  },
  [VesselStatus.CANCELLED]: {
    status: VesselStatus.CANCELLED,
    label: 'Cancelled',
    description: 'Vessel visit has been cancelled',
    color: 'danger',
    icon: 'times-circle'
  }
};

export interface ApiResponse<T> {
  data?: T;
  message?: string;
  error?: string;
  status: number;
}

export interface VesselFilters {
  status?: VesselStatus;
  vesselType?: string;
  search?: string;
  flagCountry?: string;
}
