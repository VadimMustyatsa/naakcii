import { Component, OnInit, Input,Output } from '@angular/core';

import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';
import {Cart, CartLine} from '../../../shared/cart/cart.model';
import setImgStyles from '../../../shared/utils/setImgStyles';
@Component({
  selector: 'app-cart-line',
  templateUrl: './cart-line.component.html',
  styleUrls: ['./cart-line.component.scss']
})
export class CartLineComponent implements OnInit {
  @Input() curCartline:CartLine;

  constructor(public breakPointCheckService: BreakPointCheckService,
    public cart: Cart
    ){
  }

  ngOnInit() {
  }
  subItem() {
    if (this.curCartline.quantity > 1) {
      this.cart.updateQuantity(this.curCartline.product, Number(this.curCartline.quantity - 1));
    }
  }

  addItem() {
    this.cart.updateQuantity(this.curCartline.product, Number(this.curCartline.quantity + 1));
  }

  deleteCartLine() {
    this.cart.removeLine(this.curCartline.product.id);
    // this.chainListExist = this.getExistListChain();
  }
 
  setImgStyles(pict) {
    setImgStyles(pict);
  }
}
