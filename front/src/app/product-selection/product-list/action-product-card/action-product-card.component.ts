import {Component, EventEmitter, Input, Output,OnInit} from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {FoodsStorageService} from '../../../shared/Storage/foods.storage.service';
import {Cart} from '../../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../../shared/chain/chain.model';
import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';

import {SessionStorageService} from "../../../shared/services/session-storage.service";

@Component({
  selector: 'app-action-product-card',
  templateUrl: './action-product-card.component.html',
  styleUrls: ['./action-product-card.component.scss'],
  providers: [FoodsStorageService]
})
export class ActionProductCardComponent implements OnInit{
  public selectAmount:number;

  nameMaxWidth = 80;

  @Input() product: FoodList;
 
  @Output() openModal:EventEmitter<void> = new EventEmitter();

  constructor(public  chainLst: Chain,
              public breakPointCheckService: BreakPointCheckService,
              private cart: Cart, private sessionStorageService: SessionStorageService ) {
  }

  ngOnInit(){
    this.selectAmount=1;
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

  getImgStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.imgLogoSmall;
    }
    return 'unknown';
  }

  selectFood() {
    this.cart.addLine(this.product, this.selectAmount);  //добавляем в корзину
    this.selectAmount = 1;  //сбрасываем на 1 на карточке
    if(this.emailSenderIsNotOpened){this.openModal.emit();}
  }

  get emailSenderIsNotOpened (){
    return !this.sessionStorageService.getSenderEmailOpened()
  }

  subItem() {
    if (this.selectAmount > 1) {
      this.selectAmount -= 1;
    }
  }

  addItem() {
    this.selectAmount += 1;
  }

  setImgStyles(pict) {
    return {
      'background-image': `url("assets/images/Products/${pict.img}")`,
      'background-size': 'contain',
      'background-repeat': 'no-repeat',
      'background-position': 'center'
    };
  }

  //проверяем выделена ли сеть данной карточки в фильтре сетей
  isVisibleChain(idStrore: number) {
    let selected = false;
    this.chainLst.lines.map(chain => {
      if (chain.chain.selected) {
        if (chain.chain.id == idStrore) {
          selected = true;
        }
      }
    });
    return selected;
  }
}
