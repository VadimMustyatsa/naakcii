import {Injectable} from '@angular/core';
import * as CONSTANTS from '../../CONSTANTS';


@Injectable()
export class SessionStorageService {
  constructor() {
  }

  getCartFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem(CONSTANTS.STORAGE_KEY));
  }

  setCartToSessionStorage(cart) {
    sessionStorage.setItem(CONSTANTS.STORAGE_KEY, JSON.stringify(cart));
  }

  getCartCountFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem(CONSTANTS.STORAGE_KEY_COUNT));
  }

  setCartCountToSessionStorage(count) {
    sessionStorage.setItem(CONSTANTS.STORAGE_KEY_COUNT, JSON.stringify(count));
  }

  getChainFromSessionStorage() {
    return JSON.parse(sessionStorage.getItem(CONSTANTS.CHAIN_STORAGE_KEY));
  }

  setChainToSessionStorage(chains) {
    sessionStorage.setItem(CONSTANTS.CHAIN_STORAGE_KEY, JSON.stringify(chains));
  }

  getFromUndiscountStorage() {
    return JSON.parse(sessionStorage.getItem(CONSTANTS.UNDISCOUNT_STORAGE_KEY));
  }

  setToUndiscountStorage(undiscount) {
    sessionStorage.setItem(CONSTANTS.UNDISCOUNT_STORAGE_KEY, JSON.stringify(undiscount));
  }
}
