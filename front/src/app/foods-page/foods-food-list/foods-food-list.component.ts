import {Component, Inject, OnInit} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {FoodsFoodListService} from '../../shared/foodList/foods.foodList.service';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import {Storag} from '../../shared/Storage/foods.storage.model';
import 'rxjs/add/operator/map';

@Component({
  selector: 'app-foods-food-list',
  templateUrl: './foods-food-list.component.html',
  styleUrls: ['./foods-food-list.component.css'],
  providers: [FoodsFoodListService, FoodsStorageService]
})
export class FoodsFoodListComponent implements OnInit {
  foodList: FoodList[] = [];
  private curFoodCard: FoodList;
  chainList: Storag[] = null;

  constructor(private chainService: FoodsStorageService,
              private foodsService: FoodsFoodListService,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
    console.log('FoodListComponent - constr');
  }

  ngOnInit() {
    console.log('FoodListComponent - ngOnInit');

    this.chainService.getAll().subscribe(chainList => {
      console.log(chainList);
      this.chainList = chainList;
    });

    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_SUBCATEGORY) {
        this.foodList = [];
        if (update.subCatList) {
         for (let i = 0; i < update.subCatList.length; i++) {
            if (update.subCatList[i].selected) {
              this.foodsService.getFoodList(update.category.id, update.subCatList[i].id).subscribe(productList => {
                productList.map(product => {
                  this.foodList.push(product);
                });
              });

            }
          }
        }
      }
    });
  }
}
