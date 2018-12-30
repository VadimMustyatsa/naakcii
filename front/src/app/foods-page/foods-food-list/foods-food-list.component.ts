import {Component, Inject, OnInit, HostListener} from '@angular/core';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observable} from 'rxjs/Observable';
import {FoodList} from '../../shared/foodList/foods.foodList.model';
import {FoodsFoodListService} from '../../shared/foodList/foods.foodList.service';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import 'rxjs/add/operator/map';
import {Chain} from '../../shared/chain/chain.model';
import {BreakPointCheckService} from '../../shared/services/breakpoint-check.service';

@Component({
  selector: 'app-foods-food-list',
  templateUrl: './foods-food-list.component.html',
  styleUrls: ['./foods-food-list.component.scss'],
  providers: [FoodsFoodListService, FoodsStorageService],

})
export class FoodsFoodListComponent implements OnInit {
  foodList: FoodList[] = [];
  selectedSubCatListID = [];
  countLoadCard = 0;
  firstLoadedCard = 12;
  loadedCard = 6;
  isNextCard = false;
  showLoadingCard = false;
  isFoodLength: boolean;

  constructor(public  chainLst: Chain,
              private foodsService: FoodsFoodListService,
              public breakPointCheckService: BreakPointCheckService,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
  }

  ngOnInit() {
    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_SUBCATEGORY) {
        this.foodList = [];
        this.selectedSubCatListID = [];
        if (update.subCatList) {
          for (let i = 0; i < update.subCatList.length; i++) {
            if (update.subCatList[i].selected) {
              this.selectedSubCatListID.push({id: update.subCatList[i].id});
            }
          }
          if (this.selectedSubCatListID.length > 0) {
            this.countLoadCard = 0;
            const first = this.countLoadCard;
            const last = this.firstLoadedCard;
            this.isNextCard = false;
            this.foodList.length = 0;
            this.foodsService.getFoodList(this.selectedSubCatListID, first, last).subscribe(productList => {
              productList.map(product => {
                if (!this.checkDuplicate(this.foodList, product)) {
                  this.foodList.push(product);
                }
              });
              if (productList.length == this.firstLoadedCard) {
                this.isNextCard = true;
                this.countLoadCard += this.firstLoadedCard;
                if (this.countVisibleProd() < this.firstLoadedCard) {  //необходимо догрузить
                  this.updateFoodList();
                }
              } else {
                this.isNextCard = false;
              }
            });
          }
        }
      }
      this.isFoodLength = false;
      setTimeout(() => {
        if (this.foodList && this.foodList.length === 0) {
          this.isFoodLength = true;
        }
      }, 700);
    });

  }

  // проверяем есть ли для выбранных сетей товары-----
  isVisibleProd() {
    let isProduct = false;
    this.foodList.map(food => {
      this.chainLst.lines.map(chain => {
        if (chain.chain.id === food.idStrore) {
          if (chain.chain.selected) {
            isProduct = true;
          }
        }
      });
    });
    return isProduct;
  }
  //--------------------------------------------------

  //считаем сколько в загруженных карточках есть товаров подходящих под выбранные сети
  countVisibleProd() {
    let countProduct = 0;
    this.foodList.map(food => {
      this.chainLst.lines.map(chain => {
        if (chain.chain.id === food.idStrore) {
          if (chain.chain.selected) {
            countProduct += 1;
          }
        }
      });
    });
    return countProduct;
  }
  //-----------------------------------------------------

  //проверяем есть ли хоть одна выбранная сеть-----------
  isCheckedChain() {
    let isChain = false;
    this.chainLst.lines.map(chain => {
      if (chain.chain.selected) {
        isChain = true;
      }
    });
    return isChain;
  }
  //-----------------------------------------------------

  //Догружаем следующую порцию карточек------------------
  updateFoodList() {
    if (!this.isNextCard) {
      return;
    }
    this.isNextCard = false;
    let first = this.countLoadCard;
    let last = this.countLoadCard + this.loadedCard;
    this.showLoadingCard = true;
    this.foodsService.getFoodList(this.selectedSubCatListID, first, last).subscribe(productList => {
      productList.map(product => {
        this.foodList.push(product);
      });
      if (productList.length == this.loadedCard) {
        this.isNextCard = true;
        this.countLoadCard += this.loadedCard;
        if (this.countVisibleProd() < this.firstLoadedCard) {  //необходимо догрузить
          this.updateFoodList();
        }
      } else {
        this.isNextCard = false;
      }
      this.showLoadingCard = false;
    });
  }
  //-----------------------------------------------------
  checkDuplicate(foodList, product) {
    return foodList.some(el => el.id === product.id)
  };
}
