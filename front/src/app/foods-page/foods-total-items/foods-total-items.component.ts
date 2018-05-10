import {Component, Inject, OnInit, HostListener} from '@angular/core';
import {Storag} from '../../shared/Storage/foods.storage.model';
import {FoodsStorageService} from "../../shared/Storage/foods.storage.service";
import {Cart, CartLine} from '../../shared/cart/cart.model';

@Component({
  selector: 'app-foods-total-items',
  templateUrl: './foods-total-items.component.html',
  styleUrls: ['./foods-total-items.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsTotalItemsComponent implements OnInit {
  fixedPaddingTop:number = 0;
  chainList: Storag[] = null;

  iconCollapsible = {minimized: 'keyboard_arrow_right', maximized: 'keyboard_arrow_down'};
  curentIconCollapsible = String(this.iconCollapsible.minimized);
  params = [
    {
      onOpen: (el) => {
        this.curentIconCollapsible = String(this.iconCollapsible.maximized);
      },
      onClose: (el) => {
        this.curentIconCollapsible = String(this.iconCollapsible.minimized);
      }
    }
  ];
  constructor(private chainService: FoodsStorageService,
              public cart: Cart) {}

  ngOnInit() {
    this.chainService.getAll().subscribe(chainList => {
      this.chainList = chainList;
    });
  }

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

  getStorageByID(id: number): Storag {
    return this.chainList.find(x => x.id === id);
  }
  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).name;
    }
    return 'unknown';
  }

  onEventStop(event) {
    event.stopPropagation();
  }
}
