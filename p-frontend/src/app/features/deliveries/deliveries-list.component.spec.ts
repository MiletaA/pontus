import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { DeliveriesListComponent } from './deliveries-list.component';

describe('DeliveriesListComponent', () => {
  let component: DeliveriesListComponent;
  let fixture: ComponentFixture<DeliveriesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DeliveriesListComponent,
        HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DeliveriesListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
