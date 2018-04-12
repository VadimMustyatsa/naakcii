import {Component, Inject, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {SelectFoodList} from '../../shared/foodList/foods.selectFoodList.model';

@Component({
  selector: 'app-foods-total-items',
  templateUrl: './foods-total-items.component.html',
  styleUrls: ['./foods-total-items.component.css']
})
export class FoodsTotalItemsComponent implements OnInit {
  foodTotalList: SelectFoodList[] = [];
  private curFoodCard: SelectFoodList;

  constructor(@Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
  }

  ngOnInit() {
    this.foodTotalList = [];

    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.SELECT_FOOD_CARD) {
        console.log('receive TotalItems: ' + update.foodCard.id);
        this.putToTotalList(update.foodCard);
      }
    });
  }

  putToTotalList(newFoodCard: SelectFoodList) {
    console.log('put newFoodCard.id:' + newFoodCard.id);
    if (!this.isAvailable(newFoodCard)) {
      this.putNewFoodCard(newFoodCard);
      return;
    }
  }

  isAvailable(newFoodCard: SelectFoodList): boolean {
    for (let i = 0; i < this.foodTotalList.length; i++) {
      console.log('foodTotalList.id: ' + this.foodTotalList[i].id);
      console.log('newFoodCard.id: ' + newFoodCard.id);
      if (this.foodTotalList[i].id === newFoodCard.id) {
        this.foodTotalList[i].amount += newFoodCard.amount;
        return true;
      }
    }
    return false;
  }

  putNewFoodCard(newFoodCard: SelectFoodList) {
    console.log('putNew.id: ' + newFoodCard.id);
    this.curFoodCard = new SelectFoodList;
    this.curFoodCard.id = newFoodCard.id;
    this.curFoodCard.name = newFoodCard.name;
    this.curFoodCard.allPrice = newFoodCard.allPrice;
    this.curFoodCard.discount = newFoodCard.discount;
    this.curFoodCard.totalPrice = newFoodCard.totalPrice;
    this.curFoodCard.boxWeight = newFoodCard.boxWeight;
    this.curFoodCard.idStrore = newFoodCard.idStrore;
    this.curFoodCard.img = newFoodCard.img;
    this.curFoodCard.amount = newFoodCard.amount;
    this.foodTotalList.push(this.curFoodCard);
  }

  deleteFoodCard(curFood: SelectFoodList) {
    for (let i = 0; i < this.foodTotalList.length; i++) {
      if (this.foodTotalList[i].id === curFood.id) {
        this.foodTotalList.splice(i, 1);
      }
    }
  }

  subItem(curFood: SelectFoodList) {
    if (curFood.amount > 1) {
      curFood.amount = curFood.amount - 1;
    }
  }

  addItem(curFood: SelectFoodList) {
    curFood.amount = curFood.amount + 1;
  }
}
