import { Component, Input } from '@angular/core';
import { Cargo, CUSTOMS_STATUS_INFO } from '../../../../models/cargo.model';

@Component({
  selector: 'app-cargo-status-badge',
  templateUrl: './cargo-status-badge.component.html',
  styleUrls: ['./cargo-status-badge.component.scss']
})
export class CargoStatusBadgeComponent {
  @Input() cargo!: Cargo;

  get statusInfo() {
    return CUSTOMS_STATUS_INFO[this.cargo.customsStatus];
  }
}
