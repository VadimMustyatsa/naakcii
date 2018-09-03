import {Component, OnInit, Input} from '@angular/core';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import { BreakPointCheckService } from '../../shared/services/breakpoint-check.service';
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

const titles = ['_ ведущих торговых сетей', '_ акционных товаров', '_% - средний размер достигаемой экономии'];

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
  basicData = [0, 0, 0];
  getData = 'inactive';

  constructor(private storageService: FoodsStorageService, public breakPointCheckService: BreakPointCheckService) {}

  ngOnInit() {
    this.storageService.getAll().subscribe(data => {
      this.basicData[0] = data.length;
      this.getData = 'active';
      data.forEach(item => {
        this.basicData[1] += +item.countGoods;
        this.basicData[2] += +item.percent/data.length;
      });
    });
  }

  get titles() {
    this.basicData.map( (data, index) => {
      const digit = this.basicData[1].toString().substr(-1);
      if (digit === '1'){
        titles[1] = '_ акционный товар'
      }
      if (digit === '2' || digit === '3' || digit === '4'){
        titles[1] = '_ акционныx товара'
      }
    });
    return titles.map((title, index) => title.replace('_', this.basicData[index].toFixed(0)));
  }

}

