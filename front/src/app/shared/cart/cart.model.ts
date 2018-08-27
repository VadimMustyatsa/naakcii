import {Injectable} from '@angular/core';
import {FoodList} from '../foodList/foods.foodList.model';
import {Chain, ChainLine} from '../chain/chain.model';
import {isUndefined} from "util";
import {SessionStorageService} from "../services/session-storage.service";

@Injectable()
export class Cart {
  public lines: CartLine[];
  public storageCount : {
    itemCount: number;
    cartAllPrice: number;
    cartTotalPrice: number;
    cartAverageDiscount: number;
  };
  public itemCount: number;
  public cartAllPrice: number ;
  public cartTotalPrice: number;
  public cartAverageDiscount: number;

  constructor(public  chainLst: Chain,  private sessionStorageService: SessionStorageService) {
  this.lines= this.sessionStorageService.getCartFromSessionStorage() || [];
    this.storageCount = this.sessionStorageService.getCartCountFromSessionStorage() ||
      {
        itemCount: 0,
        cartAllPrice: 0,
        cartTotalPrice: 0,
        cartAverageDiscount: 0
      };
   this.itemCount = this.storageCount.itemCount;
    this.cartAllPrice = this.storageCount.cartAllPrice;    //без скидок
    this.cartTotalPrice = this.storageCount.cartTotalPrice;  //с учетом скидок
    this.cartAverageDiscount = this.storageCount.cartAverageDiscount;    //средний процент скидки по всем карточкам
  }

  addLine(product: FoodList, quantity: number) {

    let line = this.lines.find(line => line.product.id == product.id);
    if (line != undefined) {
      line.quantity += quantity;
    } else {
      this.lines.unshift(new CartLine(product, quantity, ""));
    }
    this.recalculate();
  }

  updateQuantity(product: FoodList, quantity: number) {
    let line = this.lines.find(line => line.product.id == product.id);
    if (line != undefined) {
      line.quantity = Number(quantity);
    }
    this.recalculate();
  }

  removeLine(id: number) {
    let index = this.lines.findIndex(line => line.product.id == id);
    this.lines.splice(index, 1);
    this.recalculate();
  }

  //генерация JSON итогового списка для PDF-----------------------------------
  generateJsonListPDF() {
    let pdf = {};
    let chainSort = {};
    let totalSum = {};
    let sumBefore = 0;
    let sumAfter = 0;

    let chainListExist: ChainLine[] = [];
    this.lines.forEach(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id == line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });

    chainListExist.forEach(chain => {
      let curCartList = [];
      this.lines.forEach(cart => {
        if (chain.chain.id == cart.product.idStrore) {
          let curCart = {};
          curCart['Name'] = cart.product.name;
          curCart['Comment'] = cart.comment;
          curCart['priceOne'] = (cart.product.totalPrice).toFixed(2);
          curCart['amount'] = cart.quantity;
          curCart['priceSum'] = (cart.product.totalPrice * cart.quantity).toFixed(2);
          curCartList.push(curCart);
          if (cart.product.allPrice > 0) {
            sumBefore += cart.product.allPrice * cart.quantity;
          } else {
            sumBefore += cart.product.totalPrice * cart.quantity;
          }
          sumAfter += cart.product.totalPrice * cart.quantity;
        }
      });
      chainSort[chain.chain.name] = curCartList;
    });

    totalSum['sumBefore'] = sumBefore;
    totalSum['sumAfter'] = sumAfter;
    totalSum['discountSum'] = (sumBefore - sumAfter);
    totalSum['discountPersent'] = (100 - (sumAfter / sumBefore) * 100);

    pdf['ChainList'] = chainSort;
    pdf['totalSum'] = totalSum;
    return pdf;
  }

  getStorageByID(id: number): ChainLine {
    return this.chainLst.lines.find(x => x.chain.id === id);
  }

  //-------------------------------------------------------------------------

  clear() {
    this.lines = [];
    this.itemCount = 0;
    this.cartAllPrice = 0;
    this.cartTotalPrice = 0;
    this.cartAverageDiscount = 0;
  }

  private recalculate() {
    this.itemCount = 0;
    this.cartAllPrice = 0;
    this.cartTotalPrice = 0;
    this.cartAverageDiscount = 0;
    this.lines.forEach(l => {
      this.itemCount += l.quantity;
      if (l.product.allPrice > 0) {
        this.cartAllPrice += (l.quantity * l.product.allPrice);
      } else {
        this.cartAllPrice += (l.quantity * l.product.totalPrice);
      }
      this.cartTotalPrice += (l.quantity * l.product.totalPrice);
    });
    this.sessionStorageService.setCartToSessionStorage(this.lines);
    this.sessionStorageService.setCartCountToSessionStorage({
      itemCount: this.itemCount,
      cartAllPrice: this.cartAllPrice,
      cartTotalPrice: this.cartTotalPrice,
      cartAverageDiscount: this.cartAverageDiscount,
    });
  }
}

export class CartLine {

  constructor(public product: FoodList,
              public quantity: number,
              public comment: string) {
  }

  get lineTotal() {
    return this.quantity * this.product.totalPrice;
  }
}
