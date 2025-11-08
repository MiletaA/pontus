import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { DockCreateComponent } from './dock-create.component';

describe('DockCreateComponent', () => {
  let component: DockCreateComponent;
  let fixture: ComponentFixture<DockCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DockCreateComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DockCreateComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
