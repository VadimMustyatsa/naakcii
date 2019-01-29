import { Component, OnInit, Input } from '@angular/core';

import {BreakPointCheckService} from '../../../shared/services/breakpoint-check.service';

@Component({
  selector: 'app-cart-line',
  templateUrl: './cart-line.component.html',
  styleUrls: ['./cart-line.component.scss']
})
export class CartLineComponent implements OnInit {
  @Input() curCart
  constructor(public breakPointCheckService: BreakPointCheckService){
  }

  ngOnInit() {
    console.log(this.curCart);
  }
  
  setImgStyles(pict) {
    return {
      'background-image': `url("assets/images/Products/${pict}")`,
      'background-size': 'contain',
      'background-repeat': 'no-repeat',
      'background-position': 'center'
    };
  }
}
