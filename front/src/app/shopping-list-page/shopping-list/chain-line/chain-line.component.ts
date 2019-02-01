import { Component, OnInit,Input } from '@angular/core';

import {Cart, CartLine} from '../../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../../shared/chain/chain.model';
import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';
import setImgStyles from '../../../shared/utils/setImgStyles'

@Component({
  selector: 'app-chain-line',
  templateUrl: './chain-line.component.html',
  styleUrls: ['./chain-line.component.scss']
})
export class ChainLineComponent implements OnInit {
  public cartLineList:CartLine[];
  public chainId:number;
  @Input() chain:ChainLine;
  
  constructor(public cart: Cart,
    public breakPointCheckService: BreakPointCheckService) { 

    }

  ngOnInit() {
    this.chainId=this.chain.chain.id;
    this.cartLineList=this.cart.getCartByChain(this.chainId);
  }

  getAllPriceDiscount(){
    return this.cart.getAllPriceDiscountByChain(this.chainId);
  }
  getAllPriceBase(){
    return this.cart.getAllPriceBaseByChain(this.chainId);
  }
  getDiscountInMoney(){
    return this.cart.getAllDiscountByChainInMoney(this.chainId);
  }
  getDiscountInPercent(){
    return this.cart.getAllDiscountByChainInPercent(this.chainId);
  }

  setImgStyles(pict) {
    setImgStyles(pict);
  }

}
