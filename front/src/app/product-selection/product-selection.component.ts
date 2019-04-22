import {Component, OnInit} from '@angular/core';
import {FoodsCategoriesService} from '../shared/category/foods.categories.service';
import {Title} from "@angular/platform-browser";
import { BreakPointCheckService} from '../shared/services/breakpoint-check.service';


@Component({
  selector: 'app-foods-page',
  templateUrl: './product-selection.component.html',
  styleUrls: ['./product-selection.component.scss']
})

export class ProductSelectionComponent implements OnInit {

  constructor(private service: FoodsCategoriesService, private titleService: Title, public breakPointCheckService: BreakPointCheckService,) {
  }

  ngOnInit() {
    this.titleService.setTitle('Формирование списка покупок – НаАкции.Бел')
  }
}


