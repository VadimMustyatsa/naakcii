import { Component, OnInit} from '@angular/core';
import {FoodsCategoriesService} from '../shared/category/foods.categories.service';
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-foods-page',
  templateUrl: './foods-page.component.html',
  styleUrls: ['./foods-page.component.css']
})
export class FoodsPageComponent implements OnInit {
  constructor(private service: FoodsCategoriesService, private titleService: Title) {
  }
  ngOnInit() {
    this.titleService.setTitle('Формирование списка покупок – naakcii.by')
  }
  isSelectedCategory(): boolean {
    if (!this.service.getSelectCategory()) {
      return false;
    }
    return true;
  }
}
