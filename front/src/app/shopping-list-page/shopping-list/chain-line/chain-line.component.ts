import { Component, OnInit,Input } from '@angular/core';

import {Cart, CartLine} from '../../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../../shared/chain/chain.model';

@Component({
  selector: 'app-chain-line',
  templateUrl: './chain-line.component.html',
  styleUrls: ['./chain-line.component.scss']
})
export class ChainLineComponent implements OnInit {
  public cartLineList:CartLine[];
  public chainId:number;
  @Input() chain:ChainLine;
  
  constructor(public cart: Cart) { 

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
    return {
      'background-image': `url("assets/images/Products/${pict}")`,
      'background-size': 'contain',
      'background-repeat': 'no-repeat',
      'background-position': 'center'
    };
  }

}
