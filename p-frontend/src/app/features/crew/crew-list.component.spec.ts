import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { CrewListComponent } from './crew-list.component';

describe('CrewListComponent', () => {
  let component: CrewListComponent;
  let fixture: ComponentFixture<CrewListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CrewListComponent,
        HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CrewListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
