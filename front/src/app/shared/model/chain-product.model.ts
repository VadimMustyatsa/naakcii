import {ChainProductFromJson } from '../model/chain-product-json.model';
import {A_GOOD_PRICE, A_DISCOUNT_PERCENT, A_ONE_PLUS_ONE,
        MEASURE_KG, MEASURE_PIECE} from '../../CONSTANTS';
import {environment} from '../../../environments/environment';

export class ChainProduct {

  productId: number;
  chainId: number;
  chainName: string;
  name: string;
  unitOfMeasure: string;
  manufacturer: string;
  brand: string;
  countryOfOrigin: string;
  picture: string;
  basePrice: number;
  discountPercent: number;
  discountPrice: number;
  startDate: number;
  endDateMS: number;
  chainProductType: {name: string,
                    tooltip: string};

  changeStep: number;

  constructor(_chainProduct: ChainProductFromJson) {
    this.productId = _chainProduct['productId'];
    this.chainId = _chainProduct['chainId'];
    this.name = _chainProduct['name'];
    this.picture = environment.imgUrl + _chainProduct['picture'];
    this.basePrice = _chainProduct['basePrice'];
    this.discountPrice = _chainProduct['discountPrice'];
    this.discountPercent = _chainProduct['discountPercent'];
    this.unitOfMeasure  = _chainProduct.unitOfMeasure['name'];
    this.chainProductType = _chainProduct.chainProductType;
    this.endDateMS = _chainProduct['endDate'];
    this.changeStep = parseFloat(_chainProduct.unitOfMeasure['step']);
  }
  // есть ли у товара базовая цена
  get isConsiderBasePrice(): boolean {
    switch (this.chainProductType.name) {
      case  A_GOOD_PRICE:
        return false;
      case  A_DISCOUNT_PERCENT:
        return true;
      case A_ONE_PLUS_ONE:
        return false;
    }
  }
  // есть ли у акции для данного товара иконка
  get isIcon(): boolean {
    switch (this.chainProductType.name) {
      case  A_GOOD_PRICE:
        return true;
      case  A_DISCOUNT_PERCENT:
        return false;
      case A_ONE_PLUS_ONE:
        return false;
    }
  }

  get isFractionalPart(): boolean {
    if (this.unitOfMeasure === MEASURE_KG) {
      return true; }
    return false;
  }

  get textIcon(): string {
    switch (this.chainProductType.name) {
      case  A_GOOD_PRICE:
        return 'thumb_up';
      case  A_DISCOUNT_PERCENT:
        return `${this.discountPercent}%`;
      case A_ONE_PLUS_ONE:
        return `1+1`;
    }
  }

  get startAmount(): number {
    if (this.chainProductType.name === A_ONE_PLUS_ONE) {
      return 2;
    }
    return 1;
  }

  get endDate() {
    return new Date(this.endDateMS);
  }

  getSumWithDiscount(quantity: number): number {
    if (this.chainProductType.name === A_ONE_PLUS_ONE) {
      return this.discountPrice * quantity / 2;
    }
    return this.discountPrice * quantity;
  }
  getSumBasePrice(quantity: number): number {
    return this.basePrice * quantity;
  }

  getSumDiscountPrice(quantity: number): number {
    return this.discountPrice * quantity;
  }

  getSumDiscountInMoney(quantity: number): number {
    return this.basePrice * this.discountPercent * quantity / 100;
  }

  // определение шага изменения количества товара
  generateChangeStep() {
    let baseKf = 1;
    if (this.unitOfMeasure === MEASURE_KG) {
      baseKf = 0.1;
    }
    switch (this.chainProductType.name) {
      case  A_GOOD_PRICE:
        return baseKf;
      case  A_DISCOUNT_PERCENT:
        return baseKf;
      case A_ONE_PLUS_ONE:
        return baseKf * 2;
    }
  }

  generatechainProductType(discountPersent) {
    const promotions = [
      {name: A_GOOD_PRICE,
       tooltip: 'Товар на акции «Хорошая цена», размер скидки сетью не публикуется'},
      {name: A_ONE_PLUS_ONE,
       tooltip: 'Акция два товара по цене одного'}];
    if (discountPersent === 0) {
      return promotions[Math.floor(Math.random() * 2)];
    } else {
      return {name: A_DISCOUNT_PERCENT, tooltip: 'Товар на скидке'};
    }
  }
  genereateEndDate() {
    const currentDate = new Date();
    return currentDate.getTime() + (Math.round(Math.random() * 14 ) + 2) * 60 * 60 * 24 * 1000;
  }
}
