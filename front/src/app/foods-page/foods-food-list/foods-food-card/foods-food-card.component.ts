import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {FoodsStorageService} from '../../../shared/Storage/foods.storage.service';
import {Cart} from '../../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../../shared/chain/chain.model';
import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';
import {MaterializeAction} from "angular2-materialize";
import {SessionStorageService} from "../../../shared/services/session-storage.service";

@Component({
  selector: 'app-foods-food-card',
  templateUrl: './foods-food-card.component.html',
  styleUrls: ['./foods-food-card.component.scss'],
  providers: [FoodsStorageService]
})
export class FoodsFoodCardComponent {
  @Input() foodList: FoodList[];
  nameMaxWidth = 80;

  modalActions = new EventEmitter<string|MaterializeAction>();
  openModal() {
    this.modalActions.emit({action: 'modal', params: ['open']});
  }
  closeModal() {
    this.modalActions.emit({action: 'modal', params: ['close']});
  }

  constructor(public  chainLst: Chain,
              public breakPointCheckService: BreakPointCheckService,
              private cart: Cart, private sessionStorageService: SessionStorageService ) {

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

  selectFood(selectFood: FoodList) {
    this.cart.addLine(selectFood, selectFood.selectAmount);  //добавляем в корзину
    selectFood.selectAmount = 1;  //сбрасываем на 1 на карточке
    if(this.emailSenderIsNotOpened){this.openModal();}
  }

  get emailSenderIsNotOpened (){
    return !this.sessionStorageService.getSenderEmailOpened()
  }

  subItem(selectFood: FoodList) {
    if (selectFood.selectAmount > 1) {
      selectFood.selectAmount = selectFood.selectAmount - 1;
    }
  }

  addItem(selectFood: FoodList) {
    selectFood.selectAmount = selectFood.selectAmount + 1;
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
