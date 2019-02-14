import { Component, OnInit, Input,Output,EventEmitter } from '@angular/core';

import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';
import {Cart, CartLine} from '../../../shared/cart/cart.model';
import setImgStyles from '../../../shared/utils/setImgStyles';
@Component({
  selector: 'app-cart-line',
  templateUrl: './cart-line.component.html',
  styleUrls: ['./cart-line.component.scss']
})
export class CartLineComponent implements OnInit {
  @Input() curCartline: CartLine;
  @Output() delItem: EventEmitter<void> = new EventEmitter();

  constructor(public breakPointCheckService: BreakPointCheckService,
              public cart: Cart
    ) {
  }

  ngOnInit() {
  }
  public subItem(): void {
    if (this.curCartline.quantity > 1) {
      this.cart.updateQuantity(this.curCartline.product, Number(this.curCartline.quantity - 1));
    }
  }

  public addItem(): void {
    this.cart.updateQuantity(this.curCartline.product, Number(this.curCartline.quantity + 1));
  }

  public deleteCartLine(): void {
    this.cart.removeLine(this.curCartline.product.productId);
    this.delItem.emit();
  }

  public setImgStyles(pict: string): {} {
    return setImgStyles(pict);
  }
}
