import {Inject, Injectable} from '@angular/core';
import {FoodsStorageService} from '../Storage/foods.storage.service';
import {Storag} from '../Storage/foods.storage.model';
import {Observer} from 'rxjs/Observer';
import {SharedState, SHARED_STATE, MODES} from '../../product-selection/sharedState.model';
import {SessionStorageService} from '../services/session-storage.service';

@Injectable()
export class Chain {
  public lines: ChainLine[];
  public itemAllCount: number = 0;
  public chainAverageDiscount = 0;    // средний процент скидки по всем сетям

  constructor(private chainService: FoodsStorageService,
              private sessionStorageService: SessionStorageService,
              @Inject(SHARED_STATE) private observer: Observer<SharedState>) {
    this.lines = this.sessionStorageService.getChainFromSessionStorage()||[];
    this.chainService.getAll().subscribe(chainList => {
      chainList.map(line => {
        this.addLine(line);
      });
      // посылаем сообщение что список сетей получен
      this.observer.next(new SharedState(MODES.LOADED_CHAIN, null, null, null, null));
    });
  }

  addLine(chain: Storag) {
    const line = this.lines.find(item => item.chain.id === chain.id);
    if (line === undefined) {
      this.lines.push(new ChainLine(chain));
    }
    this.recalculate();
    this.sessionStorageService.setChainToSessionStorage(this.lines);
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

