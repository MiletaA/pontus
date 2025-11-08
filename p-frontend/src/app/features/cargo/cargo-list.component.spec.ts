import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';

import { CargoListComponent } from './cargo-list.component';
import { CargoService } from '../../core/services/cargo.service';
import { DataTableComponent } from '../../shared/components/data-table/data-table.component';

describe('CargoListComponent', () => {
  let component: CargoListComponent;
  let fixture: ComponentFixture<CargoListComponent>;
  let cargoService: jasmine.SpyObj<CargoService>;

  beforeEach(async () => {
    const cargoServiceSpy = jasmine.createSpyObj('CargoService', ['getAllCargo', 'deleteCargo']);

    await TestBed.configureTestingModule({
      imports: [
        CargoListComponent,
        HttpClientTestingModule,
        ReactiveFormsModule,
        RouterTestingModule,
        DataTableComponent
      ],
      providers: [
        { provide: CargoService, useValue: cargoServiceSpy }
      ]
    }).compileComponents();

    cargoService = TestBed.inject(CargoService) as jasmine.SpyObj<CargoService>;
    fixture = TestBed.createComponent(CargoListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load cargo on init', () => {
    const mockResponse = {
      content: [
        { 
          id: 1, 
          vesselId: 1, 
          description: 'Test Cargo', 
          weightTons: 100,
          isDangerous: false,
          customsStatus: 'PENDING',
          cargoType: 'CONTAINER',
          origin: 'Port A',
          destination: 'Port B',
          createdAt: '2024-01-01T00:00:00Z'
        }
      ],
      totalElements: 1,
      totalPages: 1,
      size: 20,
      number: 0,
      first: true,
      last: true
    };
    cargoService.getAllCargo.and.returnValue(of(mockResponse));

    component.ngOnInit();

    expect(cargoService.getAllCargo).toHaveBeenCalled();
    expect(component.cargoList).toEqual(mockResponse.content);
    expect(component.loading).toBe(false);
  });

  it('should handle error when loading cargo', () => {
    const errorMessage = 'Failed to load';
    cargoService.getAllCargo.and.returnValue(throwError(() => ({ message: errorMessage })));

    component.loadCargo();

    expect(component.error).toContain('Failed to load cargo');
    expect(component.loading).toBe(false);
    expect(component.cargoList).toEqual([]);
  });

  it('should filter cargo based on search term', () => {
    const mockResponse = {
      content: [
        { 
          id: 1, 
          vesselId: 1,
          description: 'Container Shipment', 
          origin: 'New York',
          destination: 'London',
          weightTons: 100,
          isDangerous: false,
          customsStatus: 'PENDING',
          cargoType: 'CONTAINER',
          createdAt: '2024-01-01T00:00:00Z'
        },
        { 
          id: 2, 
          vesselId: 2,
          description: 'Bulk Cargo', 
          origin: 'Los Angeles',
          destination: 'Tokyo',
          weightTons: 200,
          isDangerous: false,
          customsStatus: 'CLEARED',
          cargoType: 'BULK',
          createdAt: '2024-01-01T00:00:00Z'
        }
      ],
      totalElements: 2,
      totalPages: 1,
      size: 20,
      number: 0,
      first: true,
      last: true
    };
    cargoService.getAllCargo.and.returnValue(of(mockResponse));

    component.filterForm.patchValue({ search: 'container' });
    component.loadCargo();

    expect(component.cargoList.length).toBe(1);
    expect(component.cargoList[0].description).toContain('Container');
  });

  it('should delete cargo when confirmed', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    cargoService.deleteCargo.and.returnValue(of(void 0));
    const mockCargo: any = { 
      id: 1, 
      vesselId: 1,
      description: 'Test Cargo',
      weightTons: 100,
      isDangerous: false,
      customsStatus: 'PENDING',
      cargoType: 'CONTAINER',
      createdAt: '2024-01-01T00:00:00Z'
    };

    component.onDeleteCargo(mockCargo);

    expect(cargoService.deleteCargo).toHaveBeenCalledWith(1);
  });

  it('should not delete cargo when not confirmed', () => {
    spyOn(window, 'confirm').and.returnValue(false);
    const mockCargo: any = { 
      id: 1, 
      vesselId: 1,
      description: 'Test Cargo',
      weightTons: 100,
      isDangerous: false,
      customsStatus: 'PENDING',
      cargoType: 'CONTAINER',
      createdAt: '2024-01-01T00:00:00Z'
    };

    component.onDeleteCargo(mockCargo);

    expect(cargoService.deleteCargo).not.toHaveBeenCalled();
  });
});
