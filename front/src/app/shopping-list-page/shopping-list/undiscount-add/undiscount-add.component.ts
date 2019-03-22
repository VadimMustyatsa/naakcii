import { Component, OnInit } from '@angular/core';

import { UndiscountService } from '../../../shared/services/undiscount.service';
import setImgStyles from '../../../shared/utils/setImgStyles';

@Component( {
  selector: 'app-undiscount-add',
  templateUrl: './undiscount-add.component.html',
  styleUrls: [ './undiscount-add.component.scss' ]
} )
export class UndiscountAddComponent implements OnInit {
  undiscountProduct: string;
  public crossActive: boolean = false;

  constructor(
    private undiscountStorage: UndiscountService
  ) {
  }

  ngOnInit() {
  }

  public undiscountFieldHandler( event ): void {
    this.crossActive = event.type === 'focus';
  }

  addUndiscountProduct() {
    if ( this.undiscountProduct && this.undiscountProduct.length > 2 ) {
      this.undiscountStorage.addProduct( this.undiscountProduct );
      this.undiscountProduct = '';
    }
  }

  public setImgStyles( pict: string ): {} {
    return setImgStyles( pict );
  }
}
