import {Component, Inject, OnInit} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {FoodsFoodListService} from '../../shared/foodList/foods.foodList.service';
import {SelectFoodList} from '../../shared/foodList/foods.selectFoodList.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import {Storag} from '../../shared/Storage/foods.storage.model';

@Component({
  selector: 'app-foods-food-list',
  templateUrl: './foods-food-list.component.html',
  styleUrls: ['./foods-food-list.component.css'],
  providers: [FoodsFoodListService]
})
export class FoodsFoodListComponent implements OnInit {
  foodList: SelectFoodList[] = [];
  private curFoodCard: SelectFoodList;
  chainList: Storag[] = null;

  constructor(private chainService: FoodsStorageService,
              private foodsService: FoodsFoodListService,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
    console.log('FoodListComponent - constr');
  }

  ngOnInit() {
    console.log('FoodListComponent - ngOnInit');
    /*this.chainService.getAll().subscribe(chainList => {
      console.log(chainList);
      this.chainList = chainList;
    });*/
    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_SUBCATEGORY) {
        this.foodList = [];
        if (update.subCatList) {
          for (let i = 0; i < update.subCatList.length; i++) {
            if (update.subCatList[i].selected) {
              this.putToFoodList(this.foodsService.getFoodList(update.category.id, update.subCatList[i].id));
            }
          }
        }
      }
    });
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
