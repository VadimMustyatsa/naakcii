import { Component, OnInit } from '@angular/core';
import {FoodsCategoriesService} from '../shared/category/foods.categories.service';

@Component({
  selector: 'app-foods-page',
  templateUrl: './foods-page.component.html',
  styleUrls: ['./foods-page.component.css']
})
export class FoodsPageComponent implements OnInit {

  constructor(private service: FoodsCategoriesService) {
  }

  ngOnInit() {
  }
  isSelectedCategory(): boolean {
    if (!this.service.getSelectCategory()) {
      return false;
    }
    return true;
  }
}
