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
  chainListExist: ChainLine[] = null;
  
  constructor(public chainLst: Chain,
              public breakPointCheckService: BreakPointCheckService,
              public cart: Cart,
  ) { 
  }

  params = [
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

  getExistListChain() {
    const chainListExist: ChainLine[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id === line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });
    return chainListExist;
  }
  refreshChain(){
    this.chainListExist = this.getExistListChain();
  }
  getCartByChain(idChain: number): CartLine[] {
    return this.cart.getCartByChain(idChain);
  }

  getStorageByID(id: number): ChainLine {
    return this.chainLst.lines.find(x => x.chain.id === id);
  }

  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.name;
    }
    return 'unknown';
  }

  setImgStyles(pict) {
    return setImgStyles(pict);
  }
}
