/**
 * Cargo model interfaces matching the backend API
 */

export interface Cargo {
  id: number;
  vesselId: number;
  description: string;
  weight: number;
  isDangerous: boolean;
  customsStatus: CustomsStatus;
  origin?: string;
  destination?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface CargoCreateRequest {
  vesselId: number;
  description: string;
  weight: number;
  isDangerous: boolean;
  customsStatus: CustomsStatus;
  origin?: string;
  destination?: string;
}

export interface CargoUpdateRequest {
  vesselId?: number;
  description?: string;
  weight?: number;
  isDangerous?: boolean;
  customsStatus?: CustomsStatus;
  origin?: string;
  destination?: string;
}

export enum CustomsStatus {
  PENDING = 'PENDING',
  CLEARED = 'CLEARED',
  REJECTED = 'REJECTED',
  UNDER_INSPECTION = 'UNDER_INSPECTION'
}

export const CUSTOMS_STATUSES = Object.values(CustomsStatus);

export interface CustomsStatusInfo {
  status: CustomsStatus;
  label: string;
  description: string;
  color: string;
  icon: string;
}

export const CUSTOMS_STATUS_INFO: Record<CustomsStatus, CustomsStatusInfo> = {
  [CustomsStatus.PENDING]: {
    status: CustomsStatus.PENDING,
    label: 'Pending',
    description: 'Awaiting customs clearance',
    color: 'warning',
    icon: 'clock'
  },
  [CustomsStatus.CLEARED]: {
    status: CustomsStatus.CLEARED,
    label: 'Cleared',
    description: 'Customs clearance completed',
    color: 'success',
    icon: 'check-circle'
  },
  [CustomsStatus.REJECTED]: {
    status: CustomsStatus.REJECTED,
    label: 'Rejected',
    description: 'Customs clearance rejected',
    color: 'danger',
    icon: 'times-circle'
  },
  [CustomsStatus.UNDER_INSPECTION]: {
    status: CustomsStatus.UNDER_INSPECTION,
    label: 'Under Inspection',
    description: 'Currently being inspected by customs',
    color: 'info',
    icon: 'search'
  }
};

export interface CargoFilters {
  vesselId?: number;
  customsStatus?: CustomsStatus;
  isDangerous?: boolean;
  search?: string;
  minWeight?: number;
  maxWeight?: number;
}
