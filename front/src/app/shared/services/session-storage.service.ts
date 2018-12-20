import {Injectable} from '@angular/core';
export const SESSION_STORAGE_KEY_EMAIL = 'SESSION_STORAGE_KEY_EMAIL';
export const STORAGE_KEY = "naakciiStorage";
export const STORAGE_KEY_COUNT = "naakciiStorageCount";
export const CHAIN_STORAGE_KEY = 'naakciiChainStorage';
export const UNDISCOUNT_STORAGE_KEY = 'naakciiUndiscountStorage';


@Injectable()
export class SessionStorageService {
  constructor() {
  }

  getCartFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem(STORAGE_KEY));
  }

  setCartToSessionStorage(cart) {
    sessionStorage.setItem(STORAGE_KEY, JSON.stringify(cart));
  }

  getCartCountFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem(STORAGE_KEY_COUNT));
  }

  setCartCountToSessionStorage(count) {
    sessionStorage.setItem(STORAGE_KEY_COUNT, JSON.stringify(count))
  }

  getChainFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem(CHAIN_STORAGE_KEY));
  }

  setChainToSessionStorage(chains) {
    sessionStorage.setItem(CHAIN_STORAGE_KEY, JSON.stringify(chains));
  }

  getFromUndiscountStorage() {
    return JSON.parse(sessionStorage.getItem(UNDISCOUNT_STORAGE_KEY));
  }

  setToUndiscountStorage(undiscount) {
    sessionStorage.setItem(UNDISCOUNT_STORAGE_KEY, JSON.stringify(undiscount));
  }

  setSenderEmailOpened(email: string) {
    sessionStorage.setItem(SESSION_STORAGE_KEY_EMAIL, email);
  }

  getSenderEmailOpened():string {
    return sessionStorage.getItem(SESSION_STORAGE_KEY_EMAIL);
  }
}
