import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { VesselCreateComponent } from './vessel-create.component';

describe('VesselCreateComponent', () => {
  let component: VesselCreateComponent;
  let fixture: ComponentFixture<VesselCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        VesselCreateComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(VesselCreateComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
