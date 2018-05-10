import {Component, Inject, OnInit, HostListener} from '@angular/core';
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
  selectedSubCatListID = [];
  chainList: Storag[] = null;
  countLoadCard: number = 0;
  firstLoadedCard: number = 12;
  loadedCard: number = 6;
  isNextCard: boolean = false;
  showLoadingCard: boolean = false;

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
        this.selectedSubCatListID = [];
        if (update.subCatList) {
          for (let i = 0; i < update.subCatList.length; i++) {
            if (update.subCatList[i].selected) {
              this.selectedSubCatListID.push({id: update.subCatList[i].id});
            }
          }
          if (this.selectedSubCatListID.length > 0) {
            this.countLoadCard = 0;
            let first = this.countLoadCard;
            let last = this.firstLoadedCard;
            //console.log('first: ' + first + '; last: ' + last);
            this.isNextCard = false;
            this.foodsService.getFoodList(this.selectedSubCatListID, first, last).subscribe(productList => {
              productList.map(product => {
                this.foodList.push(product);
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
      if (update.mode === MODES.SELECT_CHAIN) {
        this.chainList = update.chainList;
      }
    });
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    if ((window.innerHeight + window.scrollY) >= (document.body.offsetHeight - 10)) {
      this.updateFoodList();
    }
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

  //----------------------------------------------

  //считаем сколько в загруженных карточках есть товаров подходящих под выбранные сети
  countVisibleProd() {
    let countProduct = 0;
    this.foodList.map(food => {
      this.chainList.map(chain => {
        if (chain.id === food.idStrore) {
          if (chain.selected) {
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
    this.chainList.map(chain => {
      if (chain.selected) {
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
    //console.log('countCard: ' + this.countLoadCard + '; first: ' + first + '; last: ' + last);
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
      //console.log('this.isNextCard = ' + this.isNextCard);
      this.showLoadingCard = false;
    });
  }

  //-----------------------------------------------------
}
