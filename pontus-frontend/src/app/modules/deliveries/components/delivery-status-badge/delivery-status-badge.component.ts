import { Component, Input } from '@angular/core';
import { Delivery, DELIVERY_STATUS_INFO } from '../../../../models/delivery.model';

@Component({
  selector: 'app-delivery-status-badge',
  templateUrl: './delivery-status-badge.component.html',
  styleUrls: ['./delivery-status-badge.component.scss']
})
export class DeliveryStatusBadgeComponent {
  @Input() delivery!: Delivery;

  get statusInfo() {
    return DELIVERY_STATUS_INFO[this.delivery.status];
  }
}
