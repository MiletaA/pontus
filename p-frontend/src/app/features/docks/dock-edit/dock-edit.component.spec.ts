import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { DockEditComponent } from './dock-edit.component';

describe('DockEditComponent', () => {
  let component: DockEditComponent;
  let fixture: ComponentFixture<DockEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DockEditComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DockEditComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
