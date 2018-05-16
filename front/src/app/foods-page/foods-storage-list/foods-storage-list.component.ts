import {Component, Input, Inject, OnInit, EventEmitter, ElementRef, HostListener} from '@angular/core';
import {MaterializeDirective, MaterializeAction} from 'angular2-materialize';
import {Storag} from '../../shared/Storage/foods.storage.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import 'rxjs/add/operator/map';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observer} from 'rxjs/Observer';
import {Chain} from '../../shared/chain/chain.model';
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-foods-storage-list',
  templateUrl: './foods-storage-list.component.html',
  styleUrls: ['./foods-storage-list.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsStorageListComponent implements OnInit {
  actionsCollapsible = new EventEmitter<string | MaterializeAction>();
  checkedAllChain: boolean;
  chainListText = '';
  curChainPercent = '';
  curChainCountGoods = '';

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
              private eRef: ElementRef,
              @Inject(SHARED_STATE) private stateEvents: Observable<SharedState>) {
    console.log('store list');
  }

  @HostListener('document:click', ['$event'])
  clickout(event) {
    if (this.eRef.nativeElement.contains(event.target)) {
      //console.log('clicked inside');
    } else {
      console.log('clicked outside');
      this.actionsCollapsible.emit({action: "collapsible", params: ['close', 0]});
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
  }

  correctAllChainsCheck() {
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
        }this.checkedAllChain = false;
      }
      //проверка на однотипность выбора
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
  }
}


