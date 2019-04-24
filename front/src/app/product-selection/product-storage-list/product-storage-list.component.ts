import { Component, Inject, OnInit, EventEmitter, ViewChild, ElementRef, HostListener, AfterViewInit } from '@angular/core';
import { MaterializeAction } from 'angular2-materialize';
import { FoodsStorageService } from '../../shared/Storage/foods.storage.service';
import 'rxjs/add/operator/map';
import { MODES, SHARED_STATE, SharedState } from '../sharedState.model';
import { Chain } from '../../shared/chain/chain.model';
import { Observable } from 'rxjs/Observable';
import { Cart } from '../../shared/cart/cart.model';
import { BreakPointCheckService } from '../../shared/services/breakpoint-check.service';
import { SessionStorageService } from '../../shared/services/session-storage.service';

@Component({
  selector: 'app-foods-storage-list',
  templateUrl: './product-storage-list.component.html',
  styleUrls: ['./product-storage-list.component.scss'],
  providers: [FoodsStorageService]
})
export class ProductStorageListComponent implements OnInit, AfterViewInit {
  actionsCollapsible = new EventEmitter<string | MaterializeAction>();
  checkedAllChain: boolean;
  chainListText = '';
  totalGoodsInfo: number[];
  @ViewChild('overlay') overlayElement: ElementRef;
  @ViewChild('shopList') shopListElement: ElementRef;


  iconCollapsible = {minimized: 'keyboard_arrow_right', maximized: 'keyboard_arrow_down'};
  currentIconCollapsible = String(this.iconCollapsible.minimized);
  params = [
    {
      onOpen: (el) => {
        this.currentIconCollapsible = String(this.iconCollapsible.maximized);
      },
      onClose: (el) => {
        this.currentIconCollapsible = String(this.iconCollapsible.minimized);
      }
    }
  ];

  constructor(public chainList: Chain,
              public cart: Cart,
              private eRef: ElementRef,
              private sessionStorageService: SessionStorageService,
              public breakPointCheckService: BreakPointCheckService,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
                this.totalGoodsInfo = [];
  }

  @HostListener('document:click', ['$event'])
  clickout(event) {
    if (this.eRef.nativeElement.contains(event.target) && event.target.className !== 'overlay') {
      this.overlayElement.nativeElement.style.display = 'none';
    } if (!this.eRef.nativeElement.contains(event.target) && event.target.className !== 'overlay') {
      this.close();
    }
  }

  ngOnInit() {
    this.stateEvents.subscribe((update) => {
      if (update.mode === MODES.LOADED_CHAIN) {
        this.correctAllChainsCheck();
      }
    });
  }

  ngAfterViewInit() {
    const num = +this.chainListText.substr(-1);
    if (!this.cart.getAllPriceDiscount() && num === 0) {
      this.overlayElement.nativeElement.style.display = 'block';
      setTimeout(() => this.actionsCollapsible.emit({action: 'collapsible', params: ['open', 0]}), 200);
    }
  }

  setAllChains(checked) {
    this.chainList.setAllChains(checked);
    if (checked) {
      this.chainListText = 'Выбраны торговые сети: все';
    } else {
      this.chainListText = 'Выберите торговую сеть';
    }
  }

  onChangeChain(id) {
    this.chainList.changeChain(id);
    this.correctAllChainsCheck();
  }

  correctAllChainsCheck() {
      if (this.chainList.lines.length > 0) {
      this.refreshCheckedChains(this.chainList.selectedIds.length);
      this.checkedAllChain = this.chainList.getIsSelectedAll();
    }
  }

  private refreshCheckedChains(cnt: number): void{
    if (cnt === 1) {
      const curChain = this.chainList.getChainById(this.chainList.selectedIds[0]).chain;
      this.chainListText = `Выбрана торговая сеть: "${curChain.name}"`;
      this.checkedAllChain = false;
    } else {
      if (cnt > 1) {
        this.chainListText = 'Выбраны торговые сети: ' + String(cnt);
      } else if (cnt === 0) {
        this.chainListText = 'Выберите торговую сеть';
      }
      if(cnt === this.chainList.lines.length){
        this.chainListText = 'Выбраны торговые сети: все';
      }
    }
  }

  onChangeAllChains() {
    this.checkedAllChain = !this.checkedAllChain;
    this.setAllChains(this.checkedAllChain);
  }

  close() {
    this.actionsCollapsible.emit({action: 'collapsible', params: ['close', 0]});
    this.currentIconCollapsible = String(this.iconCollapsible.minimized);
  }
}
