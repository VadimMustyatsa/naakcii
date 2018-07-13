import {Component, Inject, Input, OnInit} from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {FoodsStorageService} from '../../../shared/Storage/foods.storage.service';
import {Storag} from '../../../shared/Storage/foods.storage.model';
import {Cart} from '../../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../../shared/chain/chain.model';

@Component({
  selector: 'app-foods-food-card',
  templateUrl: './foods-food-card.component.html',
  styleUrls: ['./foods-food-card.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsFoodCardComponent implements OnInit {
  @Input() curFood: FoodList;
  nameMaxWidth = 85;

  constructor(public  chainLst: Chain,
              private cart: Cart) {
  }

  ngOnInit() {
    //console.log('FoodCardComponent - ngOnInit');
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
  }

  subItem(selectFood: FoodList) {
    if (selectFood.selectAmount > 1) {
      selectFood.selectAmount = selectFood.selectAmount - 1;
    }
  }

  addItem(selectFood: FoodList) {
    selectFood.selectAmount = selectFood.selectAmount + 1;
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
