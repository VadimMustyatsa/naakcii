import { Component, Inject, OnInit } from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {FoodsFoodListService} from '../../shared/foodList/foods.foodList.service';
import {SelectFoodList} from '../../shared/foodList/foods.selectFoodList.model';

@Component({
  selector: 'app-foods-food-list',
  templateUrl: './foods-food-list.component.html',
  styleUrls: ['./foods-food-list.component.css']
})
export class FoodsFoodListComponent implements OnInit {
  foodList: SelectFoodList[] = [];
  private curFoodCard: SelectFoodList;

  private service: FoodsFoodListService = new FoodsFoodListService();

  constructor(@Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
    stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_SUBCATEGORY) {
        this.foodList = [];
        for (let i = 0; i < update.subCatList.length; i++) {
          if (update.subCatList[i].selected) {
            this.putToFoodList(this.service.getFoodList(update.category.id, update.subCatList[i].id));
          }
        }
      }
    });
  }

  ngOnInit() {
  }
  putToFoodList(newFoodList: FoodList[]) {
    for (let i = 0; i < newFoodList.length; i++) {
      this.curFoodCard = new SelectFoodList;
      this.curFoodCard.id = newFoodList[i].id;
      this.curFoodCard.name = newFoodList[i].name;
      this.curFoodCard.allPrice = newFoodList[i].allPrice;
      this.curFoodCard.discount = newFoodList[i].discount;
      this.curFoodCard.totalPrice = newFoodList[i].totalPrice;
      this.curFoodCard.boxWeight = newFoodList[i].boxWeight;
      this.curFoodCard.idStrore = newFoodList[i].idStrore;
      this.curFoodCard.img = newFoodList[i].img;
      this.curFoodCard.amount = 1;
      this.foodList.push(this.curFoodCard);
    }
  }
}
