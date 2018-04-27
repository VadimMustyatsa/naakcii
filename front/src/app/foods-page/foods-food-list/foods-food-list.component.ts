import {Component, Inject, OnInit} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {FoodsFoodListService} from '../../shared/foodList/foods.foodList.service';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import {Storag} from '../../shared/Storage/foods.storage.model';
import 'rxjs/add/operator/map';
import {SubCategory} from '../../shared/subCategory/foods.subCategory.model';

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
  firstCard: number = 1;
  lastCard: number = 100;

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
        let selectedSubCatListID = [];
        if (update.subCatList) {
         for (let i = 0; i < update.subCatList.length; i++) {
            if (update.subCatList[i].selected) {
              selectedSubCatListID.push({id: update.subCatList[i].id});
            }
          }
          if (selectedSubCatListID.length > 0) {
            this.foodsService.getFoodList(update.category.id, selectedSubCatListID, this.firstCard, this.lastCard).subscribe(productList => {
              productList.map(product => {
                this.foodList.push(product);
              });
            });
          }
        }
      }
      if (update.mode === MODES.SELECT_CHAIN) {
        this.chainList = update.chainList;
        this.chainList.map(chain => {
          //console.log('id: ' + chain.id + ' - ' + chain.selected);
        });
      }
    });
  }

  //проверяем есть ли для выбранных сетей товары
  isVisibleProd() {
    let isProduct = false;
    this.foodList.map(food => {
      this.chainList.map(chain => {
        if (chain.id === food.idStrore) {
          if (chain.selected) {
            isProduct = true;
          }
        }
      });
    });
    return isProduct;
  }

  //проверяем есть ли хоть одна выбранная сеть
  isCheckedChain() {
    let isChain = false;
    this.chainList.map(chain => {
      if (chain.selected) {
        isChain = true;
      }
    });
    return isChain;
  }
}
