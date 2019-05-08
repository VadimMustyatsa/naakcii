import { Component, Input, OnInit } from '@angular/core';
import { ChainProduct } from '../../../shared/model/chain-product.model';
import { FoodsStorageService } from '../../../shared/Storage/foods.storage.service';
import { Cart } from '../../../shared/cart/cart.model';
import { Chain, ChainLine } from '../../../shared/chain/chain.model';
import { BreakPointCheckService } from '../../../shared/services/breakpoint-check.service';
import { environment } from '../../../../environments/environment';
import * as CONSTANTS  from '../../../CONSTANTS'

import roundTo2Digits from '../../../shared/utils/roundTo2Digits';
import setImgStyles from '../../../shared/utils/setImgStyles';

@Component( {
  selector: 'app-action-product-card',
  templateUrl: './action-product-card.component.html',
  styleUrls: [ './action-product-card.component.scss' ],
  providers: [ FoodsStorageService ]
} )
export class ActionProductCardComponent implements OnInit {
  public selectAmount: number;
  public readonly nameMaxWidth = 80;
  public readonly FRONT_TRIGGER_TOOLTIP: string = 'Больше информации';
  public readonly BACK_TRIGGER_TOOLTIP: string = 'Вернуться';
  public corner = 'front';

  @Input() product: ChainProduct;

  constructor( public chainLst: Chain,
               public breakPointCheckService: BreakPointCheckService,
               private cart: Cart ) {
  }


  ngOnInit() {
    this.selectAmount = this.product.startAmount;
    if ( this.isBackSideInfo ) {
      this.corner = 'front corner-front';
    }
  }

  public get isDefaultPlaceholderImage(): boolean {
    return !(this.product.picture === environment.imgUrl);
  }

  public get isBackSideInfo(): boolean {
    return !!( this.product.manufacturer || this.product.brand || this.product.countryOfOrigin );
  }

  getStorageByID( id: number ): ChainLine {
    return this.chainLst.lines.find( x => x.chain.id === id );
  }

  getNameStorage(): String {
    if ( this.getStorageByID( this.product.chainId ) ) {
      return this.getStorageByID( this.product.chainId ).chain.name;
    }
    return 'unknown';
  }

  getImgStorage(): String {
    if ( this.getStorageByID( this.product.chainId ) ) {
      return this.getStorageByID( this.product.chainId ).chain.imgLogoSmall;
    }
    return 'unknown';
  }

  selectFood() {
    this.cart.addLine( this.product, this.selectAmount );  // добавляем в корзину
    this.selectAmount = this.product.startAmount;  // сбрасываем на 1 на карточке
  }

  subItem() {
    if ( this.selectAmount > this.product.changeStep ) {
      this.selectAmount = roundTo2Digits( this.selectAmount - this.product.changeStep );
    }
  }

  addItem() {
    this.selectAmount = roundTo2Digits( this.selectAmount + this.product.changeStep );
  }

  setImgStyles() {
    return setImgStyles( this.isDefaultPlaceholderImage ? this.product.picture : CONSTANTS.PRODUCT_PLACEHOLDER_IMG);
  }

  // проверяем выделена ли сеть данной карточки в фильтре сетей
  isVisibleChain() {
    let selected = false;
    this.chainLst.lines.map( chain => {
      if ( chain.chain.selected ) {
        if ( chain.chain.id === this.product.chainId ) {
          selected = true;
        }
      }
    } );
    return selected;
  }

  public rotate( el: HTMLElement ): void {
    el.classList.toggle( 'flipped' );
  }
}
