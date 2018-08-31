import {Injectable} from '@angular/core';
import {SessionStorageService} from "./session-storage.service";

const storageKey = "naakciiStorage";
const storageKeyCount = "naakciiStorageCount";
const chainStorageKey = 'naakciiChainStorage';
const undiscountStorageKey = 'naakciiUndiscountStorage';

@Injectable()
export class UndiscountService {
  undiscount: Array<{text:string; id: string}> = [];
  constructor(private sessionStorage:SessionStorageService) {
    this.undiscount = sessionStorage.getFromUndiscountStorage() || [];
  }

  getFromUndiscount() {
    return this.undiscount;
  }

  setToUndiscount(undiscount) {
    this.undiscount = (undiscount);
    this.sessionStorage.setToUndiscountStorage(undiscount)
  }
}
