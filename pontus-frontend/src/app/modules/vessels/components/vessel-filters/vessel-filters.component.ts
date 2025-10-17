import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Vessel, VesselStatus, VesselType } from '../../../../models/vessel.model';

export interface VesselFilters {
  status?: VesselStatus;
  vesselType?: VesselType;
  flagCountry?: string;
  searchTerm?: string;
}

@Component({
  selector: 'app-vessel-filters',
  templateUrl: './vessel-filters.component.html',
  styleUrls: ['./vessel-filters.component.scss']
})
export class VesselFiltersComponent implements OnInit {
  @Input() vessels: Vessel[] = [];
  @Output() filtersChanged = new EventEmitter<VesselFilters>();

  filterForm!: FormGroup;
  vesselStatuses = Object.values(VesselStatus);
  vesselTypes = Object.values(VesselType);
  flagCountries: string[] = [];

  constructor(private fb: FormBuilder) {
    this.initializeForm();
  }

  ngOnInit(): void {
    this.extractFlagCountries();
    this.filterForm.valueChanges.subscribe(filters => {
      this.filtersChanged.emit(this.cleanFilters(filters));
    });
  }

  private initializeForm(): void {
    this.filterForm = this.fb.group({
      status: [''],
      vesselType: [''],
      flagCountry: [''],
      searchTerm: ['']
    });
  }

  private extractFlagCountries(): void {
    const countries = new Set<string>();
    this.vessels.forEach(vessel => {
      if (vessel.flagCountry) {
        countries.add(vessel.flagCountry);
      }
    });
    this.flagCountries = Array.from(countries).sort();
  }

  private cleanFilters(filters: any): VesselFilters {
    const cleaned: VesselFilters = {};
    
    if (filters.status) cleaned.status = filters.status;
    if (filters.vesselType) cleaned.vesselType = filters.vesselType;
    if (filters.flagCountry) cleaned.flagCountry = filters.flagCountry;
    if (filters.searchTerm && filters.searchTerm.trim()) {
      cleaned.searchTerm = filters.searchTerm.trim();
    }
    
    return cleaned;
  }

  clearFilters(): void {
    this.filterForm.reset();
  }
}
