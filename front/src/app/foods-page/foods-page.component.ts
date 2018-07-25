import { Component, OnInit} from '@angular/core';
import {FoodsCategoriesService} from '../shared/category/foods.categories.service';
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-foods-page',
  templateUrl: './foods-page.component.html',
  styleUrls: ['./foods-page.component.scss']
})

export class FoodsPageComponent implements OnInit {
  constructor(private service: FoodsCategoriesService, private titleService: Title) {
  }
  ngOnInit() {
    this.titleService.setTitle('Формирование списка покупок – naakcii.by')
  }
}


