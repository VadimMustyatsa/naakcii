import { Component, Inject, Input, OnInit } from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../../sharedState.model';
import {FoodsStorageService} from '../../../shared/Storage/foods.storage.service';
import {Storag} from '../../../shared/Storage/foods.storage.model';

@Component({
  selector: 'app-foods-food-card',
  templateUrl: './foods-food-card.component.html',
  styleUrls: ['./foods-food-card.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsFoodCardComponent implements OnInit {
  @Input() curFood: FoodList;
  @Input() storageList: Storag[];
  nameMaxWidth = 85;

  constructor(@Inject(SHARED_STATE) private observer: Observer<SharedState>) {
    //console.log('FoodCardComponent - constr');
  }
  ngOnInit() {
    //console.log('FoodCardComponent - ngOnInit');
  }
  getStorageByID(id: number): Storag {
    return this.storageList.find(x => x.id === id);
  }
  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).name;
    }
    return 'unknown';
  }
  getImgStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).imgLogoSmall;
    }
    return 'unknown';
  }
  selectFood(selectFood: FoodList) {
    console.log('send select: ' + selectFood.id);
    this.observer.next(new SharedState(MODES.SELECT_FOOD_CARD, null, null, selectFood));
    selectFood.selectAmount = 1;
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
    this.storageList.map(chain => {
      if (chain.selected) {
        if (chain.id == idStrore) {
          selected = true;
        }
      }
    });
    return selected;
  }
}
