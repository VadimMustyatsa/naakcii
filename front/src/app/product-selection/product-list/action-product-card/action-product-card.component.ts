import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { ChainProduct } from '../../../shared/model/chain-product.model';
import { FoodsStorageService } from '../../../shared/Storage/foods.storage.service';
import { Cart } from '../../../shared/cart/cart.model';
import { Chain, ChainLine } from '../../../shared/chain/chain.model';
import { BreakPointCheckService } from '../../../shared/services/breakpoint-check.service';

import { SessionStorageService } from '../../../shared/services/session-storage.service';
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

  @Input() product: ChainProduct;
  @Output() openModal: EventEmitter<void> = new EventEmitter();

  constructor( public chainLst: Chain,
               public breakPointCheckService: BreakPointCheckService,
               private cart: Cart, private sessionStorageService: SessionStorageService ) {
  }

  ngOnInit() {
    this.selectAmount = this.product.startAmount;
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
    if ( this.emailSenderIsNotOpened ) {
      this.openModal.emit();
    }
  }

  get emailSenderIsNotOpened() {
    return !this.sessionStorageService.getSenderEmailOpened();
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
    return setImgStyles( this.product.picture );
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
    // return selected;
    return true;
  }

  public rotate( el: HTMLElement ): void {
    el.classList.toggle( 'flipped' );
  }
}
