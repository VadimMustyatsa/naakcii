import {Component, HostListener, OnInit} from '@angular/core';
import {FoodsCategoriesService} from '../shared/category/foods.categories.service';
import {Title} from "@angular/platform-browser";
import { BreakPointCheckService} from '../shared/services/breakpoint-check.service';

@Component({
  selector: 'app-foods-page',
  templateUrl: './foods-page.component.html',
  styleUrls: ['./foods-page.component.scss']
})

export class FoodsPageComponent implements OnInit {
  constructor(private service: FoodsCategoriesService, private titleService: Title, public breakPointCheckService: BreakPointCheckService,) {
  }

  @HostListener('click', ['$event.target'])
  onClick(btn) {
    if (btn.id === 'snackbar' || btn.parentNode.id === 'snackbar') {
      document.getElementById('snackbar').classList.add('animated-hide');
    }
  }

  ngOnInit() {
    this.titleService.setTitle('Формирование списка покупок – НаАкции.Бел')
  }
}


