import {Component, OnInit,EventEmitter,} from '@angular/core';

import { Router } from '@angular/router';
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
    setImgStyles(pict);
  }
}
