import { Component, OnInit } from '@angular/core';

import {UndiscountService} from '../../../shared/services/undiscount.service';
import setImgStyles from '../../../shared/utils/setImgStyles';

@Component({
  selector: 'app-undiscount-add',
  templateUrl: './undiscount-add.component.html',
  styleUrls: ['./undiscount-add.component.scss']
})
export class UndiscountAddComponent implements OnInit {
  undiscountProduct: string;
  constructor(
    private undiscountStorage: UndiscountService
  ) { }

  ngOnInit() {
  }

  addUndiscountProduct() {
    if (this.undiscountProduct.length > 2) {
      this.undiscountStorage.addProduct(this.undiscountProduct);
      this.undiscountProduct = '';
    }
  }
  setImgStyles(pict) {
    return setImgStyles(pict);
  }
}
