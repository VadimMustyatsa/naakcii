import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';
import {Cart, CartLine} from '../../../shared/cart/cart.model';
import setImgStyles from '../../../shared/utils/setImgStyles';
import {ChainProduct} from '../../../shared/model/chain-product.model';
@Component({
  selector: 'app-cart-line',
  templateUrl: './cart-line.component.html',
  styleUrls: ['./cart-line.component.scss']
})
export class CartLineComponent implements OnInit {
  @Input() curCartline: CartLine;
  @Output() delItem: EventEmitter<void> = new EventEmitter();
  product: ChainProduct;
  constructor(public breakPointCheckService: BreakPointCheckService,
              public cart: Cart
    ) {
  }

  ngOnInit() {
    this.product = this.curCartline.product;
  }
  public subItem(): void {
    if (this.curCartline.quantity > this.product.changeStep) {
      this.cart.updateQuantity(this.product, Number(Math.round((this.curCartline.quantity - this.product.changeStep) * 10) / 10));
    }
  }

  public addItem(): void {
    this.cart.updateQuantity(this.product, Number(Math.round((this.curCartline.quantity + this.product.changeStep) * 10) / 10));
  }

  public deleteCartLine(): void {
    this.cart.removeLine(this.product.productId);
    this.delItem.emit();
  }

  public setImgStyles(pict: string): {} {
    return setImgStyles(pict);
  }
}
