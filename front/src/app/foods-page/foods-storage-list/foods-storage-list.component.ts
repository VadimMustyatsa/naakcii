import { Component, Inject, OnInit, EventEmitter, ViewChild, ElementRef, HostListener, AfterViewInit } from '@angular/core';
import { MaterializeAction } from 'angular2-materialize';
import { Storag } from '../../shared/Storage/foods.storage.model';
import { FoodsStorageService } from '../../shared/Storage/foods.storage.service';
import 'rxjs/add/operator/map';
import { MODES, SHARED_STATE, SharedState } from '../sharedState.model';
import { Chain } from '../../shared/chain/chain.model';
import { Observable } from 'rxjs/Observable';
import { Cart, CartLine } from '../../shared/cart/cart.model';
import { BreakPointCheckService } from '../../shared/services/breakpoint-check.service';
import {SessionStorageService} from "../../shared/services/session-storage.service";

@Component({
  selector: 'app-foods-storage-list',
  templateUrl: './foods-storage-list.component.html',
  styleUrls: ['./foods-storage-list.component.scss'],
  providers: [FoodsStorageService]
})
export class FoodsStorageListComponent implements OnInit, AfterViewInit {
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

  constructor(public  chainLst: Chain,
              public cart: Cart,
              private eRef: ElementRef,
              private sessionStorageService:SessionStorageService,
              public breakPointCheckService: BreakPointCheckService,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
  }

  @HostListener('document:click', ['$event'])
  clickout(event) {
    if (this.eRef.nativeElement.contains(event.target) && event.target.className !== 'overlay') {
      this.overlayElement.nativeElement.style.display = 'none';
    } if (!this.eRef.nativeElement.contains(event.target) && event.target.className !== 'overlay') {
      this.actionsCollapsible.emit({action: 'collapsible', params: ['close', 0]});
      this.curentIconCollapsible = String(this.iconCollapsible.minimized);
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
    if (!this.cart.cartTotalPrice && num === 0) {
      this.overlayElement.nativeElement.style.display = 'block';
      setTimeout(() => this.actionsCollapsible.emit({action: 'collapsible', params: ['open', 0]}), 200);
    }
  }

  setAllChains(checked) {
    this.chainLst.lines.map(el => {
      el.chain.selected = checked;
    });
    if (checked) {
      this.chainListText = 'Выбраны торговые сети: все';
      this.curChainPercent = '';
      this.curChainCountGoods = '';
    } else {
      this.chainListText = 'Выберите торговую сеть';
      this.curChainPercent = '';
      this.curChainCountGoods = '';
    }
    this.checkedAllChain = checked;
  }

  onChangeChain(id) {
    this.chainLst.lines.map(el => {
      if (el.chain.id === id) {
        el.chain.selected = !el.chain.selected;
      }
    });
    this.correctAllChainsCheck();
   this.sessionStorageService.setChainToSessionStorage(this.chainLst.lines);
  }

  correctAllChainsCheck() {
     sessionStorage.setItem('naakciiChainStorage', JSON.stringify(this.chainLst.lines));
    if (this.chainLst.lines.length > 0) {
      let cnt = 0;
      let curChain: Storag;
      this.chainLst.lines.map(chain => {
        if (chain.chain.selected) {
          cnt += 1;
          curChain = chain.chain;
        }
      });
      if (cnt === 1) {
        this.chainListText = String(curChain.name);
        this.curChainPercent = String(curChain.percent) + ' %';
        this.curChainCountGoods = String(curChain.countGoods);
        this.checkedAllChain = false;
      } else {
        this.curChainPercent = '';
        this.curChainCountGoods = '';
        if (cnt > 1) {
          this.chainListText = 'Выбраны торговые сети: ' + String(cnt);
        } else if (cnt === 1) {
          this.chainListText = String(curChain.name);
          this.curChainPercent = String(curChain.percent) + ' %';
          this.curChainCountGoods = String(curChain.countGoods);
        } else if (cnt === 0) {
          this.chainListText = 'Выберите торговую сеть';
          this.checkedAllChain = false;
        }
          this.checkedAllChain = false;
      }
      // проверка на однотипность выбора
      const curCheck = this.chainLst.lines[0].chain.selected;
      for (let i = 1; i < this.chainLst.lines.length; i++) {
        if (curCheck !== this.chainLst.lines[i].chain.selected) {
          return;
        }
      }
      this.setAllChains(curCheck);
    }
  }

  onChangeAllChains() {
    this.checkedAllChain = !this.checkedAllChain;
    this.setAllChains(this.checkedAllChain);
    this.sessionStorageService.setChainToSessionStorage(this.chainLst.lines);
  }
}
