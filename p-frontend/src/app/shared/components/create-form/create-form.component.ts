import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface FormField {
  key: string;
  label: string;
  type: 'text' | 'number' | 'email' | 'date' | 'select' | 'checkbox' | 'textarea';
  required?: boolean;
  options?: { value: any; label: string }[];
  placeholder?: string;
}

@Component({
  selector: 'app-create-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-form.component.html',
  styleUrls: ['./create-form.component.scss']
})
export class CreateFormComponent {
  @Input() title: string = 'Create New Item';
  @Input() fields: FormField[] = [];
  @Input() loading: boolean = false;
  
  @Output() onSubmit = new EventEmitter<any>();
  @Output() onCancel = new EventEmitter<void>();
  
  formData: any = {};
  
  ngOnInit() {
    // Initialize form data with default values
    this.fields.forEach(field => {
      if (field.type === 'checkbox') {
        this.formData[field.key] = false;
      } else {
        this.formData[field.key] = '';
      }
    });
  }
  
  submitForm() {
    this.onSubmit.emit(this.formData);
  }
}
