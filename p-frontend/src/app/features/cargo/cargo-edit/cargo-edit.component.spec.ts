import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { CargoEditComponent } from './cargo-edit.component';

describe('CargoEditComponent', () => {
  let component: CargoEditComponent;
  let fixture: ComponentFixture<CargoEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CargoEditComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CargoEditComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
