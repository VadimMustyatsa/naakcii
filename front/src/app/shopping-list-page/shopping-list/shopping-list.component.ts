import {Component, OnInit} from '@angular/core';

import {isUndefined} from 'util';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {BreakPointCheckService} from '../../shared/services/breakpoint-check.service';
import {Cart, CartLine} from '../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../shared/chain/chain.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import {UndiscountService} from '../../shared/services/undiscount.service';

@Component({
  selector: 'app-shopping-list',
  templateUrl: './shopping-list.component.html',
  styleUrls: ['./shopping-list.component.scss'],
  providers: [FoodsStorageService],
})

export class ShoppingListComponent implements OnInit {
  undiscountProduct: any;
  chainListExist: ChainLine[] = null;
  undiscount: Array<{ text: string; id: string }>;

  constructor(public chainLst: Chain,
              public breakPointCheckService: BreakPointCheckService,
              private undiscountStorage: UndiscountService,
              public cart: Cart,
  ) { 
    this.undiscount = this.undiscountStorage.getFromUndiscount() || [];
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


  getCartAllPriceByChain(idChain: number) {
    return this.cart.getCartAllPriceByChain(idChain);
  }

  getCartTotalPriceByChain(idChain: number) {
    return this.cart.getCartTotalPriceByChain(idChain);
  }

  getSumDiscount(food: FoodList) {
    return (food.allPrice - food.totalPrice).toFixed(2);
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

  deleteCartLine(cartLine: CartLine) {
    this.cart.removeLine(cartLine.product.id);
    this.chainListExist = this.getExistListChain();
  }

  subItem(curFood: CartLine) {
    if (curFood.quantity > 1) {
      this.cart.updateQuantity(curFood.product, Number(curFood.quantity - 1));
    }
  }

  addItem(curFood: CartLine) {
    this.cart.updateQuantity(curFood.product, Number(curFood.quantity + 1));
  }


  onRemoveUndiscount(event){
     this.undiscount.forEach((i,index)=>{
       if (event.target.parentNode.id === i.id.toString()){
         this.undiscount.splice(index,1);
       }
     });
    this.undiscountStorage.setToUndiscount(this.undiscount);
  }

  addUndiscountProduct() {
    const { undiscountProduct } = this;
    if (undiscountProduct.length > 2) {
      this.undiscount.push({
        text: undiscountProduct,
        id: (+(new Date())).toString()
      });
      this.undiscountProduct = '';
    }
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
