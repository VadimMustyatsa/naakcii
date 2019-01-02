import { Component, OnInit } from '@angular/core';
import { BreakPointCheckService } from '../../shared/services/breakpoint-check.service';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';
import {HomePageService} from '../home-page-service/home-page.service';

const titles = ['_ ведущих торговых сетей', '_ акционных товаров', '_% средний размер достигаемой экономии'];

@Component({
  selector: 'app-home-slider',
  templateUrl: './home-slider.component.html',
  styleUrls: ['./home-slider.component.scss'],
  animations: [
    trigger('getData', [
      state('inactive', style({
        opacity: 0
      })),
      state('active',   style({
        opacity: 1
      })),
      transition('inactive => active', animate('500ms ease-in')),
    ])
  ]
})
export class HomeSliderComponent implements OnInit {
  totalGoodsInfo = [];
  getData = 'inactive';
  titleHeaders = titles;

  constructor(private homePageService: HomePageService,
    public breakPointCheckService: BreakPointCheckService) {
    }

  ngOnInit() {
    this.homePageService.statistics.subscribe(data => {
      ({ chainQuantity: this.totalGoodsInfo[0], discountedProducts: this.totalGoodsInfo[1], averageDiscountPercentage: this.totalGoodsInfo[2] } = data);
      this.getData = 'active';
      this.matchPhrase();
    });
  }

  get titles() {
    return this.titleHeaders;
  }

  matchPhrase() {
    this.totalGoodsInfo.map(() => {
      const digit = this.totalGoodsInfo[1].toString().substr(-1);
      if (digit === '1') {
        titles[1] = '_ акционный товар';
      }
      if (digit === '2' || digit === '3' || digit === '4') {
        titles[1] = '_ акционныx товара';
      }
    });
    this.titleHeaders = titles.map((title, index) => title.replace('_', this.totalGoodsInfo[index].toFixed(0)));
  }

}


