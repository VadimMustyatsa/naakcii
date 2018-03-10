import { Component, Inject, OnInit } from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {FoodsFoodListService} from '../../shared/foodList/foods.foodList.service';

@Component({
  selector: 'app-foods-food-list',
  templateUrl: './foods-food-list.component.html',
  styleUrls: ['./foods-food-list.component.css']
})
export class FoodsFoodListComponent implements OnInit {
  foodList: FoodList[] = [];
  curFoodList: FoodList[] = [];
  private service: FoodsFoodListService = new FoodsFoodListService();

  constructor(@Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
    stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_SUBCATEGORY) {
        this.foodList = [];
        for (let i = 0; i < update.subCatList.length; i++) {
          if (update.subCatList[i].selected) {
            console.log('catID: ' + update.category.id + '; subID: ' + update.subCatList[i].id);
            this.curFoodList = this.service.getFoodList(update.category.id, update.subCatList[i].id);
            for (let j = 0; j < this.curFoodList.length; j++) {
              this.foodList.push(this.curFoodList[j]);
            }
          }
        }
      }
    });
  }

  ngOnInit() {
  }

}
