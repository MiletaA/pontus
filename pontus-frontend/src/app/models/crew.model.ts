/**
 * Crew model interfaces matching the backend API
 */

export interface CrewMember {
  id: number;
  firstName: string;
  lastName: string;
  nationality: string;
  rank: string;
  passportNumber: string;
  certificateNumber?: string;
  vesselId?: number;
  createdAt: string;
  updatedAt?: string;
}

export interface CrewCreateRequest {
  firstName: string;
  lastName: string;
  nationality: string;
  rank: string;
  passportNumber: string;
  certificateNumber?: string;
  vesselId?: number;
}

export interface CrewUpdateRequest {
  firstName?: string;
  lastName?: string;
  nationality?: string;
  rank?: string;
  passportNumber?: string;
  certificateNumber?: string;
  vesselId?: number;
}

export interface CrewFilters {
  nationality?: string;
  rank?: string;
  vesselId?: number;
  search?: string;
}

// Common maritime ranks
export const CREW_RANKS = [
  'Captain',
  'Chief Officer',
  'Second Officer',
  'Third Officer',
  'Chief Engineer',
  'Second Engineer',
  'Third Engineer',
  'Bosun',
  'Able Seaman',
  'Ordinary Seaman',
  'Cook',
  'Steward',
  'Radio Officer',
  'Electrician',
  'Oiler',
  'Wiper'
];

// Common nationalities in maritime industry
export const COMMON_NATIONALITIES = [
  'Philippines',
  'India',
  'China',
  'Indonesia',
  'Ukraine',
  'Russia',
  'Myanmar',
  'Turkey',
  'Bangladesh',
  'Romania',
  'Poland',
  'Croatia',
  'Greece',
  'Bulgaria',
  'Serbia',
  'Montenegro',
  'Georgia',
  'Latvia',
  'Lithuania',
  'Estonia'
];
