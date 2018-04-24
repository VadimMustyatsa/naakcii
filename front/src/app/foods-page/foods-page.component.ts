import { Component, OnInit, HostListener } from '@angular/core';
import {FoodsCategoriesService} from '../shared/category/foods.categories.service';

@Component({
  selector: 'app-foods-page',
  templateUrl: './foods-page.component.html',
  styleUrls: ['./foods-page.component.css']
})
export class FoodsPageComponent implements OnInit {
  isFixedBlock: boolean = false;

  constructor(private service: FoodsCategoriesService) {
  }

  @HostListener('window:scroll', ['$event']) onScrollEvent($event){
    const verticalOffset = window.pageYOffset
      || document.documentElement.scrollTop
      || document.body.scrollTop || 0;
    if (verticalOffset > 320) {
      //this.isFixedBlock = true;
    } else {
      //this.isFixedBlock = false;
    }
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
