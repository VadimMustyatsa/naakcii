import {
  Component,
  ElementRef,
  OnInit,
  ChangeDetectionStrategy,
  HostListener,
  EventEmitter
} from '@angular/core';
import {Title} from '@angular/platform-browser';
import {MaterializeAction} from 'angular2-materialize';

import {BreakPointCheckService} from '../shared/services/breakpoint-check.service';
import {Cart} from '../shared/cart/cart.model';

@Component({
  selector: 'app-finalize-page',
  templateUrl: './shopping-list-page.component.html',
  styleUrls: ['./shopping-list-page.component.scss'],

  changeDetection: ChangeDetectionStrategy.OnPush

})
export class ShoppingListPageComponent implements OnInit {

  modalActions = new EventEmitter<string | MaterializeAction>();

  constructor(private titleService: Title,
              public cart: Cart,
              public breakPointCheckService: BreakPointCheckService
    ) {
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

  onEventStop(event) {
    event.stopPropagation();
  }

  openModal() {
    this.modalActions.emit({action: 'modal', params: ['open']});
  }
}
