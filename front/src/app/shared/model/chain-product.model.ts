import {FoodList} from '../foodList/foods.foodList.model';
import {A_GOOD_PRICE, A_DISCOUNT_PERCENT, A_ONE_PLUS_ONE,
        MEASURE_KG, MEASURE_PIECE} from '../../CONSTANTS';

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
  endDate: number;
  chainProductType: {name: string,
                    tooltip: string};

  constructor(_chainProduct: FoodList,  private idCategory: number) {
    // console.log(_chainProduct);
    this.productId = _chainProduct['id'];
    this.chainId = _chainProduct['chainId'];
    this.name = _chainProduct['name'];
    this.picture = _chainProduct['picture'].replace('%', '%25');
    this.basePrice = _chainProduct['price'];
    this.discountPrice = _chainProduct['discountPrice'];
    this.discountPercent = _chainProduct['discount'];
    this.unitOfMeasure = this.generateMeasure(); // генерируется пока API 1
    this.chainProductType = this.generatechainProductType(_chainProduct['discount']);
    this.endDate = this.genereateEndDate();
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
  getSumWithDiscount(quantity: number): number {
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

  // на основе категории присваивает весовой или штучный !убрать после включения API 2!
  generateMeasure() {
    if ((this.idCategory === 1002) ||
     (this.idCategory === 1003)) {
      return MEASURE_KG;
    }
    return MEASURE_PIECE;
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
    return currentDate.getTime() + (Math.floor(Math.random() * 14 ) + 2) * 60 * 60 * 24;
  }
}
