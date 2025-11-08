import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { CargoCreateComponent } from './cargo-create.component';

describe('CargoCreateComponent', () => {
  let component: CargoCreateComponent;
  let fixture: ComponentFixture<CargoCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CargoCreateComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CargoCreateComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
