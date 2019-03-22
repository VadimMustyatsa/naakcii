import { Component, Inject, OnInit, EventEmitter, ViewChild, ElementRef, HostListener, AfterViewInit } from '@angular/core';
import { MaterializeAction } from 'angular2-materialize';
import { Storag } from '../../shared/Storage/foods.storage.model';
import { FoodsStorageService } from '../../shared/Storage/foods.storage.service';
import 'rxjs/add/operator/map';
import { MODES, SHARED_STATE, SharedState } from '../sharedState.model';
import { Chain } from '../../shared/chain/chain.model';
import { Observable } from 'rxjs/Observable';
import { Cart } from '../../shared/cart/cart.model';
import { BreakPointCheckService } from '../../shared/services/breakpoint-check.service';

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
  curChainPercent = '';
  curChainCountGoods = '';
  @ViewChild('overlay') overlayElement: ElementRef;
  @ViewChild('shopList') shopListElement: ElementRef;


  iconCollapsible = {minimized: 'keyboard_arrow_right', maximized: 'keyboard_arrow_down'};
  curentIconCollapsible = String(this.iconCollapsible.minimized);
  params = [
    {
      onOpen: (el) => {
        this.curentIconCollapsible = String(this.iconCollapsible.maximized);
      },
      onClose: (el) => {
        this.curentIconCollapsible = String(this.iconCollapsible.minimized);
      }
    }
  ];

  constructor(public chainLst: Chain,
              public cart: Cart,
              private eRef: ElementRef,
              public breakPointCheckService: BreakPointCheckService,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
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
    if (this.chainLst.lines.length > 0) {
      this.correctAllChainsCheck();
      return;
    }
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
    this.chainLst.setAllChains(checked);
    if (checked) {
      this.chainListText = 'Выбраны торговые сети: все';
      this.curChainPercent = '';
      this.curChainCountGoods = '';
    } else {
      this.chainListText = 'Выберите торговую сеть';
      this.curChainPercent = '';
      this.curChainCountGoods = '';
    }
  }

  onChangeChain(id) {
    this.chainLst.changeChain(id);
    this.correctAllChainsCheck();
  }

  correctAllChainsCheck() {
    if (this.chainLst.lines.length > 0) {
      const cnt = this.chainLst.selectedIds.length;
      if (cnt === 1) {
        const curChain = this.chainLst.getChainById(this.chainLst.selectedIds[0]).chain;
        this.chainListText = `Выбрана торговая сеть: "${curChain.name}"`;
        this.checkedAllChain = false;
        this.chainListText = curChain.name;
        this.curChainPercent = `${curChain.percent}%`;
        this.curChainCountGoods = String(curChain.countGoods);
      } else {
        this.curChainPercent = '';
        this.curChainCountGoods = '';
        if (cnt > 1) {
          this.chainListText = 'Выбраны торговые сети: ' + String(cnt);
        } else if (cnt === 0) {
          this.chainListText = 'Выберите торговую сеть';
        }
      }
      this.checkedAllChain = this.chainLst.getIsSelectedAll();

      // проверка на однотипность выбора
      // const curCheck = this.chainLst.lines[0].chain.selected;
      // for (let i = 1; i < this.chainLst.lines.length; i++) {
      //   if (curCheck !== this.chainLst.lines[i].chain.selected) {
      //     return;
      //   }
      // }
      // this.setAllChains(curCheck);
    }
  }


  onChangeAllChains() {
    this.checkedAllChain = !this.checkedAllChain;
    this.setAllChains(this.checkedAllChain);
  }

  close() {
    this.actionsCollapsible.emit({action: 'collapsible', params: ['close', 0]});
    this.curentIconCollapsible = String(this.iconCollapsible.minimized);
  }
}
