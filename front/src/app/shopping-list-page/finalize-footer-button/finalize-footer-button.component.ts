import { Component, OnInit,EventEmitter,Output } from '@angular/core';

import {BreakPointCheckService} from '../../shared/services/breakpoint-check.service';
import {PdfGeneratorService } from '../../shared/services/pdf-generator.service';
import {Cart} from '../../shared/cart/cart.model';
@Component({
  selector: 'app-finalize-footer-button',
  templateUrl: './finalize-footer-button.component.html',
  styleUrls: ['./finalize-footer-button.component.scss']
})
export class FinalizeFooterButtonComponent implements OnInit {
  @Output() openModal:EventEmitter<void> = new EventEmitter();

  constructor(public breakPointCheckService: BreakPointCheckService,
              private PDFGenerator: PdfGeneratorService,
              public cart: Cart

    ) { 

  }

  ngOnInit() {
  }

  generatePDF() {
    if (this.cart.getCount()>0){
      this.PDFGenerator.generatePDF();
      this.openModal.emit();
    }
  }
}
