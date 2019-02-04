import { Component, OnInit } from '@angular/core';

import {Cart} from '../../shared/cart/cart.model';
import {UndiscountService} from '../../shared/services/undiscount.service';
import {BreakPointCheckService} from '../../shared/services/breakpoint-check.service';

@Component({
  selector: 'app-finalize-footer',
  templateUrl: './finalize-footer.component.html',
  styleUrls: ['./finalize-footer.component.scss']
})
export class FinalizeFooterComponent implements OnInit {
  undiscount: Array<{ text: string; id: string }>;
  constructor(public cart: Cart,
              private undiscountStorage: UndiscountService,
              public breakPointCheckService: BreakPointCheckService,
    ) {
    this.undiscount = this.undiscountStorage.getFromUndiscount() || [];
  }

  ngOnInit() {
  }
  getAllPriceBase(): number {
    return this.cart.getAllPriceBase();
  }
  getAllPriceDiscont(): number {
    return this.cart.getAllPriceDiscount();
  }
  getAllDiscountInMoney(): number {
    return this.cart.getAllDiscountInMoney();
  }
  getAllDiscountInPercent(): number {
    return this.cart.getAllDiscountInPercent();
  }
}
