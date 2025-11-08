import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { VesselEditComponent } from './vessel-edit.component';

describe('VesselEditComponent', () => {
  let component: VesselEditComponent;
  let fixture: ComponentFixture<VesselEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        VesselEditComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(VesselEditComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
