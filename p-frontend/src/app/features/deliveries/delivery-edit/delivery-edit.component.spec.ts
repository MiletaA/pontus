import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { DeliveryEditComponent } from './delivery-edit.component';

describe('DeliveryEditComponent', () => {
  let component: DeliveryEditComponent;
  let fixture: ComponentFixture<DeliveryEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DeliveryEditComponent,
        HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DeliveryEditComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
