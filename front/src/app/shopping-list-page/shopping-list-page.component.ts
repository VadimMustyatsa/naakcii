import {
  Component,
  ElementRef,
  OnInit,
  EventEmitter,
  ChangeDetectionStrategy,
  HostListener
} from '@angular/core';

import {MaterializeAction} from 'angular2-materialize';
import {Title} from '@angular/platform-browser';
import { Router } from '@angular/router';
import { PdfGeneratorService } from '../shared/services/pdf-generator.service';
import { UndiscountService } from '../shared/services/undiscount.service';
import {Cart} from '../shared/cart/cart.model';

@Component({
  selector: 'app-finalize-page',
  templateUrl: './shopping-list-page.component.html',
  styleUrls: ['./shopping-list-page.component.scss'],
  
  changeDetection: ChangeDetectionStrategy.OnPush

})
export class ShoppingListPageComponent implements OnInit {

  params = [
    {
      onOpen: (el) => {
        el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_drop_down';
        console.log('open')
      },
      onClose: (el) => {
        el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_right';
        console.log('close')
      }
    }
  ];
  modalActions = new EventEmitter<string | MaterializeAction>();

  openModal() {
    this.modalActions.emit({action: 'modal', params: ['open']});
  }

  closeModal() {
    this.modalActions.emit({action: 'modal', params: ['close']});
  }

  constructor(private router: Router,
              //private el: ElementRef,
              private titleService: Title,
              private PDFGenerator: PdfGeneratorService,
              private undiscountStorage: UndiscountService,
              public cart: Cart) {
    
  }

  @HostListener('click', ['$event.target'])
  onClick(btn) {
    if (btn.id === 'snackbar' || btn.parentNode.id === 'snackbar') {
      document.getElementById('snackbar').classList.add('animated-hide');
    }
  }

  ngOnInit() {
    this.titleService.setTitle('Список покупок – НаАкции.Бел');
  }

 
  onRedirect() {
    this.closeModal();
    sessionStorage.clear();
    this.undiscountStorage.clearUndiscount();
    this.cart.lines = [];
    this.router.navigateByUrl('/form-shopping-list');
  }

  generatePDF() {
    this.PDFGenerator.generatePDF();
  }

  onEventStop(event) {
    event.stopPropagation();
  }
}


