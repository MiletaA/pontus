import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';

import { DocksListComponent } from './docks-list.component';

describe('DocksListComponent', () => {
  let component: DocksListComponent;
  let fixture: ComponentFixture<DocksListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        DocksListComponent,
        HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DocksListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
