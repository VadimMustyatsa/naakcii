import {Injectable} from '@angular/core';
import {SessionStorageService} from './session-storage.service';

const storageKey = 'naakciiStorage';
const storageKeyCount = 'naakciiStorageCount';
const chainStorageKey = 'naakciiChainStorage';
const undiscountStorageKey = 'naakciiUndiscountStorage';

@Injectable()
export class UndiscountService {
  undiscount: Array<{text: string; id: string}> = [];
  constructor(private sessionStorage: SessionStorageService) {
    this.undiscount = sessionStorage.getFromUndiscountStorage() || [];
  }

  getFromUndiscount() {
    return this.undiscount;
  }
  saveToStorage() {
    this.sessionStorage.setToUndiscountStorage(this.undiscount);
  }
  setToUndiscount(undiscount) {
    this.undiscount = (undiscount);
    this.saveToStorage();
  }
  clearUndiscount() {
    this.undiscount = [];
  }
  addProduct(text: string) {
    this.undiscount.push({
      text: text,
      id: ((new Date()).getTime()).toString()
    });
  }

  delProduct(id: string) {
    this.undiscount.forEach((el, index) => {
      if (el.id === id) {
        this.undiscount.splice(index, 1);
      }
    });
    this.saveToStorage();
  }

  getCount(): number {
    return this.undiscount.length;
  }
}
