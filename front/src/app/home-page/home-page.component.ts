import { Component, OnInit, HostListener } from '@angular/core';
import {Title, Meta} from "@angular/platform-browser";
import {
  trigger,
  state,
  style,
  animate,
  transition
} from '@angular/animations';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.scss'],
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
export class HomePageComponent implements OnInit {

  getData = 'inactive';

  constructor(private titleService: Title, private metaService: Meta) {
    this.metaService.addTag({name:"description",content:"Цифровой сервис, предоставляющий возможность составить свой список покупок преимущественно (или полностью) из акционных товаров торговых сетей (товаров со скидкой) Республики Беларусь и сэкономить до 20-30% от общей суммы закупки."});
    this.metaService.addTag({name:"keywords",content:"на акции бел, на акции бай, скидки, список покупок, торговые сети, акционные товары, минск, беларусь"});
  }

  ngOnInit() {
    this.titleService.setTitle('Сервис экономии на розничных закупках – НаАкции.Бел');
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    if (document.body.scrollTop > 100 || document.documentElement.scrollTop > 100) {
      document.getElementById('up-btn').style.display = 'block';
      this.getData = 'active';
    } else {
      document.getElementById('up-btn').style.display = 'none';
      this.getData = 'inactive';
    }
  }
}
