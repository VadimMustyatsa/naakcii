import {Component, Inject, OnInit, HostListener} from '@angular/core';
import {Storag} from '../../shared/Storage/foods.storage.model';
import {FoodsStorageService} from "../../shared/Storage/foods.storage.service";
import {Cart, CartLine} from '../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../shared/chain/chain.model';

@Component({
  selector: 'app-foods-total-items',
  templateUrl: './foods-total-items.component.html',
  styleUrls: ['./foods-total-items.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsTotalItemsComponent implements OnInit {
  fixedPaddingTop:number = 0;

  iconCollapsible = {minimized: 'keyboard_arrow_right', maximized: 'keyboard_arrow_down'};
  curentIconCollapsible = String(this.iconCollapsible.minimized);
  targets = document.getElementsByClassName('.storkRight')[0];
  params = [
    {
      onOpen: (el) => {
        if (document.getElementsByClassName('active')[1]) {
          this.targets = document.getElementsByClassName('active')[1];
          let collection = document.getElementsByClassName('storkRight');
          for (let i = 0; i < collection.length; i++) {
            collection[i].innerHTML = this.iconCollapsible.minimized;
          }
          this.targets.querySelector('.storkRight').innerHTML = String(this.iconCollapsible.maximized);
        }
      },
      onClose: (el) => {
        if (this.targets && this.targets !== document.getElementsByClassName('active')[1]) {
          this.targets.querySelector('.storkRight').innerHTML = String(this.iconCollapsible.minimized);
        }
      }
    }
  ];
  constructor(public  chainLst: Chain,
              public cart: Cart) {}

  ngOnInit() { }

  @HostListener('window:scroll', ['$event']) onScrollEvent($event){
    const verticalOffset = window.pageYOffset
      || document.documentElement.scrollTop
      || document.body.scrollTop || 0;
    if (verticalOffset > 280) {
      this.fixedPaddingTop = verticalOffset - 280;
    } else {
      this.fixedPaddingTop = 0;
    }
  }

  deleteFoodCard(curFood: CartLine) {
    this.cart.removeLine(curFood.product.id);
  }
  subItem(curFood: CartLine) {
    if (curFood.quantity > 1) {
      this.cart.updateQuantity(curFood.product, Number(curFood.quantity - 1));
    }
  }
  addItem(curFood: CartLine) {
    this.cart.updateQuantity(curFood.product, Number(curFood.quantity + 1));
  }

  getPaddingTop() {
    return (String(this.fixedPaddingTop) + 'px');
  }

  getStorageByID(id: number): ChainLine {
    if (this.chainLst.lines.length > 0) {
      return this.chainLst.lines.find(x => x.chain.id === id);
    }
  }
  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.name;
    }
    return 'unknown';
  }

  onEventStop(event) {
    event.stopPropagation();
  }
}
