import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { FormsModule } from '@angular/forms';

import { CrewCreateComponent } from './crew-create.component';

describe('CrewCreateComponent', () => {
  let component: CrewCreateComponent;
  let fixture: ComponentFixture<CrewCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CrewCreateComponent,
        HttpClientTestingModule, RouterTestingModule, FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CrewCreateComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
