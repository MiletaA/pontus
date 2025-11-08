import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { DeliveryCreateComponent } from './delivery-create.component';

describe('DeliveryCreateComponent', () => {
  let component: DeliveryCreateComponent;
  let fixture: ComponentFixture<DeliveryCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DeliveryCreateComponent,
        HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DeliveryCreateComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
