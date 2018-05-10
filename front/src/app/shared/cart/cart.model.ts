import { Injectable } from '@angular/core';
import {FoodList} from '../foodList/foods.foodList.model';


@Injectable()
export class Cart {
  public lines: CartLine[] = [];
  public itemCount: number = 0;
  public cartAllPrice: number = 0;    //без скидок
  public cartTotalPrice: number = 0;  //с учетом скидок
  public cartAverageDiscount = 0;    //средний процент скидки по всем карточкам

  addLine(product: FoodList, quantity: number) {
    let line = this.lines.find(line => line.product.id == product.id);
    if (line != undefined) {
      line.quantity += quantity;
    } else {
      this.lines.push(new CartLine(product, quantity));
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
    this.lines.splice(index,1);
    this.recalculate();
  }

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
    })
  }
}

export class CartLine {

  constructor(public product: FoodList,
              public quantity: number) {}

  get lineTotal() {
    return this.quantity * this.product.totalPrice;
  }
}
