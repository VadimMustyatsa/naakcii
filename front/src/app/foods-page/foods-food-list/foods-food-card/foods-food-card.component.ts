import {Component, Input, OnInit} from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {FoodsStorageService} from '../../../shared/Storage/foods.storage.service';
import {Cart} from '../../../shared/cart/cart.model';
import {Chain, ChainLine} from '../../../shared/chain/chain.model';
import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';

@Component({
  selector: 'app-foods-food-card',
  templateUrl: './foods-food-card.component.html',
  styleUrls: ['./foods-food-card.component.scss'],
  providers: [FoodsStorageService]
})
export class FoodsFoodCardComponent implements OnInit {
  @Input() foodList: FoodList[];
  nameMaxWidth = 80;

  constructor(public  chainLst: Chain,
              public breakPointCheckService: BreakPointCheckService,
              private cart: Cart) {

  }

  ngOnInit() {
    // если продуктов нечетное количество, добавляем пустышку в массив для кастомного центрирования, т.к. поведение justify-content: center не удовлетворяет
    if (this.foodList.length % 2 !== 0) {
      this.foodList.push(this.foodList[0]);
    }
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
