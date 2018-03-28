import { Component, Inject, Input, OnInit } from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../../sharedState.model';
import {SelectFoodList} from '../../../shared/foodList/foods.selectFoodList.model';
import {FoodsStorageService} from '../../../shared/Storage/foods.storage.service';
import {Storag} from '../../../shared/Storage/foods.storage.model';

@Component({
  selector: 'app-foods-food-card',
  templateUrl: './foods-food-card.component.html',
  styleUrls: ['./foods-food-card.component.css']
})
export class FoodsFoodCardComponent implements OnInit {
  storageList: Storag[];
  private service: FoodsStorageService = new FoodsStorageService;
  @Input() curFood: SelectFoodList;

  constructor(@Inject(SHARED_STATE) private observer: Observer<SharedState>) { }

  ngOnInit() {
    this.storageList = this.service.getAll();
  }
  getStorageByID(id: number): Storag {
    // console.log('getStorageByID: ' + id);
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
  selectFood(selectFood: SelectFoodList) {
    console.log('send select: ' + selectFood.id);
    this.observer.next(new SharedState(MODES.SELECT_FOOD_CARD, null, null, selectFood));
    selectFood.amount = 1;
  }
  subItem(selectFood: SelectFoodList) {
    if (selectFood.amount > 1) {
      selectFood.amount = selectFood.amount - 1;
    }
  }
  addItem(selectFood: SelectFoodList) {
    selectFood.amount = selectFood.amount + 1;
  }
}
