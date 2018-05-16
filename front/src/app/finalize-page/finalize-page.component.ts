import {Component, OnInit} from '@angular/core';
import {Cart, CartLine} from '../shared/cart/cart.model';
import {FoodsStorageService} from '../shared/Storage/foods.storage.service';
import {Storag} from '../shared/Storage/foods.storage.model';
import {isUndefined} from 'util';
import {FoodList} from '../shared/foodList/foods.foodList.model';
import {Chain, ChainLine} from '../shared/chain/chain.model';

@Component({
  selector: 'app-finalize-page',
  templateUrl: './finalize-page.component.html',
  styleUrls: ['./finalize-page.component.css'],
  providers: [FoodsStorageService]
})
export class FinalizePageComponent implements OnInit {
  chainListExist: ChainLine[] = null;

  constructor(public  chainLst: Chain,
              public cart: Cart) {
  }

  ngOnInit() {
    this.chainListExist = this.getExistListChain();
  }

  getExistListChain() {
    let chainListExist: ChainLine[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id == line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });
    return chainListExist;
  }

  getCartByChain(idChain: number): CartLine[] {
    let cartListByChain: CartLine[] = [];
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        cartListByChain.push(line);
      }
    });
    return cartListByChain;
  }

  getCartQuantityByChain(idChain: number) {
    let quantity = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        quantity += line.quantity;
      }
    });
    return quantity;
  }
  getCartAllPriceByChain(idChain: number) {
    let allPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        if (line.product.allPrice > 0) {
          allPrice += (line.product.allPrice*line.quantity);
        } else  {
          allPrice += (line.product.totalPrice*line.quantity);
        }
      }
    });
    return allPrice;
  }
  getCartTotalPriceByChain(idChain: number) {
    let totalPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        totalPrice += (line.product.totalPrice*line.quantity);
      }
    });
    return totalPrice;
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
}
