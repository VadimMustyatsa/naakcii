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
  constructor(private undiscountStorage: UndiscountService,
              public cart: Cart,
              public breakPointCheckService: BreakPointCheckService,
    ) {
    this.undiscount = this.undiscountStorage.getFromUndiscount() || [];
  }

  ngOnInit() {
  }
  public getAllPriceBase(): number {
    return this.cart.getAllPriceBase();
  }
  public getAllPriceDiscont(): number {
    return this.cart.getAllPriceDiscount();
  }
  public getAllDiscountInMoney(): number {
    return this.cart.getAllDiscountInMoney();
  }
  public getAllDiscountInPercent(): number {
    return this.cart.getAllDiscountInPercent();
  }
}
