import {Inject, Injectable} from '@angular/core';
import {FoodsStorageService} from '../Storage/foods.storage.service';
import {Storag} from '../Storage/foods.storage.model';
import {Observer} from 'rxjs/Observer';
import {SharedState, SHARED_STATE, MODES} from '../../foods-page/sharedState.model';
const chainStorageKey = 'naakciiChainStorage';

@Injectable()
export class Chain {
  public lines: ChainLine[] = JSON.parse(localStorage.getItem(chainStorageKey))||[];
  public itemAllCount: number = 0;
  public chainAverageDiscount = 0;    //средний процент скидки по всем сетям

  constructor(private chainService: FoodsStorageService,
              @Inject(SHARED_STATE) private observer: Observer<SharedState>) {
    this.chainService.getAll().subscribe(chainList => {
      chainList.map(line => {
        this.addLine(line);
      });
      console.log(this.lines);
      //посылаем сообщение что список сетей получен
      this.observer.next(new SharedState(MODES.LOADED_CHAIN, null, null, null, null));
    });
  }

  addLine(chain: Storag) {
    let line = this.lines.find(line => line.chain.id == chain.id);
    if (line == undefined) {
      this.lines.push(new ChainLine(chain));
    }
    this.recalculate();
    localStorage.setItem(chainStorageKey, JSON.stringify(this.lines));
  }

  clear() {
    this.lines = [];
    this.itemAllCount = 0;
    this.chainAverageDiscount = 0;
  }

  private recalculate() {
    this.itemAllCount = 0;
    this.chainAverageDiscount = 0;

    this.lines.forEach(l => {
      this.itemAllCount += l.chain.countGoods;
      this.chainAverageDiscount += l.chain.percent;
    });
    this.chainAverageDiscount = Math.ceil(this.chainAverageDiscount / this.lines.length);
  }
}

export class ChainLine {
  constructor(public chain: Storag) {}
}

