import {Injectable} from '@angular/core';
const storageKey = "naakciiStorage";
const storageKeyCount = "naakciiStorageCount";
const chainStorageKey = 'naakciiChainStorage';

@Injectable()
export class SessionStorageService {
  constructor() {
  }
  getCartFromSessionStorage(){
    return JSON.parse(sessionStorage.getItem(storageKey));
  }
  setCartToSessionStorage(cart){
    sessionStorage.setItem(storageKey, JSON.stringify(cart));
  }
  getCartCountFromSessionStorage(){
    return JSON.parse(sessionStorage.getItem(storageKeyCount));
  }
  setCartCountToSessionStorage(count){
     sessionStorage.setItem(storageKeyCount, JSON.stringify(count))
  }
  getChainFromSessionStorage(){
return JSON.parse(sessionStorage.getItem(chainStorageKey));
  }
  setChainToSessionStorage(chains){
    sessionStorage.setItem(chainStorageKey, JSON.stringify(chains));
  }
}
