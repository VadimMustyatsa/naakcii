import { Component, Input, Inject, OnInit, EventEmitter } from '@angular/core';
import {MaterializeDirective, MaterializeAction} from 'angular2-materialize';
import {Storag} from '../../shared/Storage/foods.storage.model';
import {FoodsStorageService} from '../../shared/Storage/foods.storage.service';
import 'rxjs/add/operator/map';
import {MODES, SHARED_STATE, SharedState} from '../sharedState.model';
import {Observer} from 'rxjs/Observer';

@Component({
  selector: 'app-foods-storage-list',
  templateUrl: './foods-storage-list.component.html',
  styleUrls: ['./foods-storage-list.component.css'],
  providers: [FoodsStorageService]
})
export class FoodsStorageListComponent implements OnInit {
  chainList: Storag[];
  medianGoods;
  medianPercent;
  checkedAllChain = true; //выбор всеx сетей по-умолчанию
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

  constructor(private service: FoodsStorageService, @Inject(SHARED_STATE) private observer: Observer<SharedState>) {
    console.log('store list');
  }

  ngOnInit() {
    console.log('store list');
    this.medianGoods = 0;
    this.medianPercent = 0;
    this.service.getAll().subscribe(chainList => {
      console.log(chainList);
      this.chainList = chainList;
      this.setAllChains(this.checkedAllChain);
      chainList.map(el => {
        this.medianGoods += el['countGoods'];
        this.medianPercent += el['percent'];
      });
      this.medianPercent = Math.ceil(this.medianPercent / chainList.length);
    });
  }

  setAllChains(checked) {
    this.checkedAllChain = checked;
    this.chainList.map(el => {
      el.selected = checked;
    });
    if (checked) {
      this.chainListText = 'Выбраны торовые сети: все';
      this.curChainPercent = '';
      this.curChainCountGoods = '';
    } else {
      this.chainListText = 'Выберите торговую сеть';
      this.curChainPercent = '';
      this.curChainCountGoods = '';
    }
    //посылаем в поле карточек список выбранных сетей
    this.observer.next(new SharedState(MODES.SELECT_CHAIN, null, null, null, this.chainList));
  }

  onChangeChain(id) {
    this.chainList.map(el => {
      if (el.id === id) {
        el.selected = !el.selected;
      }
    });
    this.correctAllChainsCheck();
  }

  correctAllChainsCheck() {
    if (this.chainList) {
      let cnt = 0;
      let curChain: Storag;
      this.chainList.map(chain => {
        if (chain.selected) {
          cnt += 1;
          curChain = chain;
        }
      });
      if (cnt === 1) {
        this.chainListText = String(curChain.name);
        this.curChainPercent = String(curChain.percent) + ' %';
        this.curChainCountGoods = String(curChain.countGoods);
      } else {
        this.curChainPercent = '';
        this.curChainCountGoods = '';
        if (cnt > 1) {
          this.chainListText = 'Выбраны торовые сети: ' + String(cnt);
        } else if (cnt === 1) {
          this.chainListText = String(curChain.name);
          this.curChainPercent = String(curChain.percent) + ' %';
          this.curChainCountGoods = String(curChain.countGoods);
        } else if (cnt === 0) {
          this.chainListText = 'Выберите торговую сеть';
        }
      }
      const curCheck = this.chainList[0].selected;
      for (let i = 1; i < this.chainList.length; i++) {
        if (curCheck !== this.chainList[i].selected) {
          this.checkedAllChain = false;
          this.observer.next(new SharedState(MODES.SELECT_CHAIN, null, null, null, this.chainList));
          return;
        }
      }
      this.setAllChains(curCheck);
    }
    //посылаем в поле карточек список выбранных сетей
    this.observer.next(new SharedState(MODES.SELECT_CHAIN, null, null, null, this.chainList));
  }

  onChangeAllChains() {
    this.checkedAllChain = !this.checkedAllChain;
    this.setAllChains(this.checkedAllChain);
  }
}

