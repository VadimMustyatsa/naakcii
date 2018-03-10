import { Component, Inject, Input, OnInit } from '@angular/core';
import {FoodList} from '../../../shared/foodList/foods.foodList.model';
import {Observer} from 'rxjs/Observer';
import {MODES, SHARED_STATE, SharedState} from '../../sharedState.model';

@Component({
  selector: 'app-foods-food-card',
  templateUrl: './foods-food-card.component.html',
  styleUrls: ['./foods-food-card.component.css']
})
export class FoodsFoodCardComponent implements OnInit {
  @Input() curFood;

  constructor(@Inject(SHARED_STATE) private observer: Observer<SharedState>) { }

  ngOnInit() {
  }
  selectFood(selectFood: FoodList) {
    console.log('send select: ' + selectFood.id);
    this.observer.next(new SharedState(MODES.SELECT_FOOD_CARD, null, null, selectFood));
  }
}
