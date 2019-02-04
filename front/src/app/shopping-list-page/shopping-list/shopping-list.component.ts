import {Component, OnInit} from '@angular/core';
import {isUndefined} from 'util';

import {BreakPointCheckService} from '../../shared/services/breakpoint-check.service';
import {Cart, CartLine} from '../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../shared/chain/chain.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import setImgStyles from '../../shared/utils/setImgStyles';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss'],
  providers: [FoodsStorageService],
})

export class ShoppingListComponent implements OnInit {
  public chainListExist: ChainLine[] = null;

  constructor(public chainLst: Chain,
              public breakPointCheckService: BreakPointCheckService,
              public cart: Cart,
  ) {
  }

  public params = [
    {
      onOpen: (el) => {
        el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_drop_down';
      },
      onClose: (el) => {
        el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_right';
      }
    }
  ];

  ngOnInit() {
    this.chainListExist = this.getExistListChain();
  }

  private getExistListChain() {
    const chainListExist: ChainLine[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id === line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });
    return chainListExist;
  }
  public refreshChain(): void {
    this.chainListExist = this.getExistListChain();
  }
  public getCartByChain(idChain: number): CartLine[] {
    return this.cart.getCartByChain(idChain);
  }

  public getStorageByID(id: number): ChainLine {
    return this.chainLst.lines.find(x => x.chain.id === id);
  }

  public getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.name;
    }
    return 'unknown';
  }

  public setImgStyles(pict: string): {} {
    return setImgStyles(pict);
  }
}
