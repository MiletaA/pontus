/**
 * Delivery model interfaces matching the backend API
 */

export interface Delivery {
  id: number;
  cargoId: number;
  driverName: string;
  vehiclePlate: string;
  destination: string;
  scheduledDate: string;
  actualDate?: string;
  status: DeliveryStatus;
  notes?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface DeliveryCreateRequest {
  cargoId: number;
  driverName: string;
  vehiclePlate: string;
  destination: string;
  scheduledDate: string;
  notes?: string;
}

export interface DeliveryUpdateRequest {
  cargoId?: number;
  driverName?: string;
  vehiclePlate?: string;
  destination?: string;
  scheduledDate?: string;
  actualDate?: string;
  status?: DeliveryStatus;
  notes?: string;
}

export enum DeliveryStatus {
  SCHEDULED = 'SCHEDULED',
  IN_TRANSIT = 'IN_TRANSIT',
  DELIVERED = 'DELIVERED',
  DELAYED = 'DELAYED',
  CANCELLED = 'CANCELLED'
}

export const DELIVERY_STATUSES = Object.values(DeliveryStatus);

export interface DeliveryStatusInfo {
  status: DeliveryStatus;
  label: string;
  description: string;
  color: string;
  icon: string;
}

export const DELIVERY_STATUS_INFO: Record<DeliveryStatus, DeliveryStatusInfo> = {
  [DeliveryStatus.SCHEDULED]: {
    status: DeliveryStatus.SCHEDULED,
    label: 'Scheduled',
    description: 'Delivery is planned and scheduled',
    color: 'secondary',
    icon: 'calendar-alt'
  },
  [DeliveryStatus.IN_TRANSIT]: {
    status: DeliveryStatus.IN_TRANSIT,
    label: 'In Transit',
    description: 'Delivery is currently in progress',
    color: 'info',
    icon: 'truck'
  },
  [DeliveryStatus.DELIVERED]: {
    status: DeliveryStatus.DELIVERED,
    label: 'Delivered',
    description: 'Delivery has been completed successfully',
    color: 'success',
    icon: 'check-circle'
  },
  [DeliveryStatus.DELAYED]: {
    status: DeliveryStatus.DELAYED,
    label: 'Delayed',
    description: 'Delivery is behind schedule',
    color: 'warning',
    icon: 'clock'
  },
  [DeliveryStatus.CANCELLED]: {
    status: DeliveryStatus.CANCELLED,
    label: 'Cancelled',
    description: 'Delivery has been cancelled',
    color: 'danger',
    icon: 'times-circle'
  }
};

export interface DeliveryFilters {
  cargoId?: number;
  status?: DeliveryStatus;
  search?: string;
  scheduledFrom?: string;
  scheduledTo?: string;
}
