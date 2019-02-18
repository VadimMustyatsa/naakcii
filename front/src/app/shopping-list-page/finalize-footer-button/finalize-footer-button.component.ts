import { Component, OnInit, EventEmitter, Output } from '@angular/core';

import {BreakPointCheckService} from '../../shared/services/breakpoint-check.service';
import {PdfGeneratorService } from '../../shared/services/pdf-generator.service';
import {Cart} from '../../shared/cart/cart.model';
@Component({
  selector: 'app-finalize-footer-button',
  templateUrl: './finalize-footer-button.component.html',
  styleUrls: ['./finalize-footer-button.component.scss']
})
export class FinalizeFooterButtonComponent implements OnInit {
  @Output() openModal: EventEmitter<void> = new EventEmitter();

  constructor(private PDFGenerator: PdfGeneratorService,
              public breakPointCheckService: BreakPointCheckService,
              public cart: Cart
    ) {
  }

  ngOnInit() {
  }

  public generatePDF(): void {
    if (this.cart.getCount() > 0) {
      this.PDFGenerator.generatePDF();
      this.openModal.emit();
    }
  }
  public openModalEv(): void {
    if (this.cart.getCount() > 0) {
    }
  }
}
