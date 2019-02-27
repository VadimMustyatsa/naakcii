import {Component, OnInit, HostListener} from '@angular/core';
import { FoodsStorageService } from '../../shared/Storage/foods.storage.service';
import {Cart, CartLine} from '../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../shared/chain/chain.model';
import { BreakPointCheckService } from '../../shared/services/breakpoint-check.service';

import roundTo2Digits from '../../shared/utils/roundTo2Digits';

@Component({
  selector: 'app-foods-total-items',
  templateUrl: './products-total.component.html',
  styleUrls: ['./products-total.component.scss'],
  providers: [FoodsStorageService]
})
export class ProductsTotalComponent implements OnInit {

  params = [
    {
      onOpen: (el) => {
          el.prevObject[0].querySelector('.arrowCollapsibleLite').innerHTML = 'keyboard_arrow_down';
      },
      onClose: (el) => {
        if ( el.prevObject[0]) {
          el.prevObject[0].querySelector('.arrowCollapsibleLite').innerHTML = 'keyboard_arrow_right';
        }
      }
    }
  ];
  constructor(public  chainLst: Chain, public breakPointCheckService: BreakPointCheckService,
              public cart: Cart) {}

  ngOnInit() {
  }

  deleteFoodCard(curFood: CartLine) {
    this.cart.removeLine(curFood.product.productId);
  }
  subItem(curFood: CartLine) {
    if (curFood.quantity > curFood.product.changeStep) {
      this.cart.updateQuantity(curFood.product, roundTo2Digits(curFood.quantity - curFood.product.changeStep));
    }
  }
  addItem(curFood: CartLine) {
    this.cart.updateQuantity(curFood.product, roundTo2Digits(curFood.quantity + curFood.product.changeStep));
  }

  getStorageByID(id: number): ChainLine {
    if (this.chainLst.lines.length > 0) {
      return this.chainLst.lines.find(x => x.chain.id === id);
    }
  }
  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.name;
    }
    return 'unknown';
  }

  onEventStop(event) {
    event.stopPropagation();
  }
}
