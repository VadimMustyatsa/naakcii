import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home-layer1',
  templateUrl: './home-layer1.component.html',
  styleUrls: ['./home-layer1.component.scss']
})
export class HomeLayer1Component implements OnInit {
  goFoodsBtn = 'Перейти к товарам';

  constructor() { }

  ngOnInit() {
  }

}
