import { Component, OnInit,Input } from '@angular/core';
import { Router } from '@angular/router';

import { UndiscountService } from '../../shared/services/undiscount.service';
import {Cart} from '../../shared/cart/cart.model';

@Component({
  selector: 'app-modal-final',
  templateUrl: './modal-final.component.html',
  styleUrls: ['./modal-final.component.scss']
})
export class ModalFinalComponent implements OnInit {
  @Input() modalActions;
  constructor(private undiscountStorage: UndiscountService,
              private router: Router,
              private cart: Cart
    ) { }

  ngOnInit() {
  }
  closeModal() {
    this.modalActions.emit({action: 'modal', params: ['close']});
  }

  onRedirect() {
    this.closeModal();
    sessionStorage.clear();
    this.undiscountStorage.clearUndiscount();
    this.cart.clear();
    this.router.navigateByUrl('/form-shopping-list');
  }
}
