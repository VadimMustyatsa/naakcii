import { Inject, Injectable } from '@angular/core';
import { FoodsStorageService } from '../Storage/foods.storage.service';
import { Storag } from '../Storage/foods.storage.model';
import { Observer } from 'rxjs/Observer';
import { SharedState, SHARED_STATE, MODES } from '../../product-selection/sharedState.model';
import { SessionStorageService } from '../services/session-storage.service';
import { FoodsFoodListService } from '../foodList/foods.foodList.service';
import { HomePageService } from '../../home-page/home-page-service/home-page.service';

@Injectable()
export class Chain {
  public lines: ChainLine[];
  public selectedIds: number[];
  public isSelectAll: boolean;
  public itemAllCount: number = 0;
  public chainAverageDiscount = 0;    // средний процент скидки по всем сетям
  public chainQuantity: number;

  constructor( private chainService: FoodsStorageService,
               private sessionStorageService: SessionStorageService,
               private chainProductService: FoodsFoodListService,
               private homePageService: HomePageService,
               @Inject( SHARED_STATE ) private observer: Observer<SharedState> ) {
    this.lines = this.sessionStorageService.getChainFromSessionStorage() || [];
    this.selectedIds = [];
    this.chainService.getAll().subscribe( chainList => {
      chainList.map( line => {
        this.addLine( line );
      } );
      this.changeSelectedIds();
      // посылаем сообщение что список сетей получен
      this.observer.next( new SharedState( MODES.LOADED_CHAIN, null, null, null, null ) );
    } );

    this.homePageService.statistics.subscribe( data => {
      ( {
        chainQuantity: this.chainQuantity,
        discountedProducts: this.itemAllCount,
        averageDiscountPercentage: this.chainAverageDiscount
      } = data );
    } );
  }


  addLine( chain: Storag ) {
    const line = this.lines.find( item => item.chain.id === chain.id );
    if ( line === undefined ) {
      this.lines.push( new ChainLine( chain ) );
    }
    this.sessionStorageService.setChainToSessionStorage( this.lines );
  }

  changeSelectedIds() {
    this.selectedIds = this.lines.filter( el => {
      return el.chain.selected;
    } ).map( el => {
      return el.chain.id;
    } );
    if ( this.selectedIds.length === this.lines.length ) {
      this.isSelectAll = true;
    } else {
      this.isSelectAll = false;
    }
    this.chainProductService.changeChainList( this.selectedIds );
  }

  changeChain( id: number ) {
    this.lines.forEach( el => {
      if ( el.chain.id === id ) {
        el.chain.selected = !el.chain.selected;
      }
    } );
    this.sessionStorageService.setChainToSessionStorage( this.lines );
    this.changeSelectedIds();
  }

  getChainById( id: number ): ChainLine {
    return this.lines.find( el => {
      return el.chain.id === id;
    } );

  }

  getIsSelectedAll(): boolean {
    return this.isSelectAll;
  }

  clear() {
    this.lines = [];
    this.itemAllCount = 0;
    this.chainAverageDiscount = 0;
  }

  setAllChains( checked: boolean ): void {
    this.lines.forEach( el => {
      el.chain.selected = checked;
    } );
    this.sessionStorageService.setChainToSessionStorage( this.lines );
    this.changeSelectedIds();
  }
}

export class ChainLine {
  constructor( public chain: Storag ) {
  }
}

