import {Component, OnInit} from '@angular/core';
import * as jsPDF from 'jspdf';
import * as html2canvas from 'html2canvas';
import {Cart, CartLine} from '../shared/cart/cart.model';
import {FoodsStorageService} from '../shared/Storage/foods.storage.service';
import {Storag} from '../shared/Storage/foods.storage.model';
import {isUndefined} from "util";
import {FoodList} from "../shared/foodList/foods.foodList.model";

@Component({
  selector: 'app-finalize-page',
  templateUrl: './finalize-page.component.html',
  styleUrls: ['./finalize-page.component.css'],
  providers: [FoodsStorageService]
})
export class FinalizePageComponent implements OnInit {
  chainList: Storag[] = null;
  chainListExist: Storag[] = null;
  math: Math;

  constructor(private chainService: FoodsStorageService,
              public cart: Cart) {
  }

  ngOnInit() {
    this.chainService.getAll().subscribe(chainList => {
      this.chainList = chainList;
      this.chainListExist = this.getExistListChain();
    });
  }

  getExistListChain() {
    let chainListExist: Storag[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.id == line.product.idStrore))) {
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
          allPrice += (line.product.allPrice * line.quantity);
        } else {
          allPrice += (line.product.totalPrice * line.quantity);
        }
      }
    });
    return allPrice;
  }

  getCartTotalPriceByChain(idChain: number) {
    let totalPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        totalPrice += (line.product.totalPrice * line.quantity);
      }
    });
    return totalPrice;
  }

  getSumDiscount(food: FoodList) {
    return (food.allPrice - food.totalPrice).toFixed(2);
  }

  getStorageByID(id: number): Storag {
    return this.chainList.find(x => x.id === id);
  }

  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).name;
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

  createPDF() {
    html2canvas(document.getElementById('result')).then(function (canvas) {
      console.log(document.getElementById('result').scrollHeight);
      let resize = document.getElementById('result').scrollHeight / 200;
      var img = canvas.toDataURL("image/png");
      var doc = new jsPDF('', 'mm', 'A4');
      doc.addImage(img, 'JPEG', 5, 5, 200, (30 * resize + 10));
      doc.save('testCanvas.pdf');
    });
  }
}
