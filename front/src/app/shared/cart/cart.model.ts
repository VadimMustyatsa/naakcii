import {Injectable} from '@angular/core';
// import {FoodList} from '../foodList/foods.foodList.model';
import {ChainProduct} from '../../shared/model/chain-product.model';
import {Chain, ChainLine} from '../chain/chain.model';
import {isUndefined} from 'util';
import {SessionStorageService} from '../services/session-storage.service';

@Injectable()
export class Cart {
  public lines: CartLine[];

  private sumAllBasePrice: number ;
  private sumAllDiscountPrice: number;
  private sumDiscountInMoney: number ;
  private sumDiscountInPercent: number;
  private isAllHaveBasePrice: boolean;
  constructor(public  chainLst: Chain,  private sessionStorageService: SessionStorageService) {
  const linesJSON = this.sessionStorageService.getCartFromSessionStorage() || [];
  this.lines = linesJSON.map(line => {
    line.__proto__ = CartLine.prototype;
    line.product.__proto__ = ChainProduct.prototype;
    return line;
  });
  this.recalculate();
  }

   // суммарная стоимость товаров у которых известна цена до скидки
  private culcSumBasePrice(cartLineList: CartLine[]): number {
    return cartLineList.reduce((sum: number, line: CartLine) => {
      if (line.product.isConsiderBasePrice) {
        return sum + line.product.getSumBasePrice(line.quantity);
      } else {
        return sum;
      }
    }, 0);
  }

  // суммарная стоимость с учетом скидки
  private culcSumDiscountPrice(cartLineList: CartLine[]): number {
    return cartLineList.reduce((sum, line) => {
      return sum + line.product.getSumWithDiscount(line.quantity);
    }, 0);
  }

  // сумма скидки по товарам у которых известна цена до скидки
  private culcDiscountInMoney(cartLineList: CartLine[]): number {
    return cartLineList.reduce((curDiscount, line) => {
      if (line.product.isConsiderBasePrice) {
        return curDiscount + line.product.getSumDiscountInMoney(line.quantity);
      }
    }, 0);
  }

  // расчет скидки в процентах, для товаров у которых есть начальная цена
  private culcDiscountInPercent(cartLineList: CartLine[]): number {
    const curDiscount = this.culcDiscountInMoney(cartLineList);
    const basePrice = this.culcSumBasePrice(cartLineList);
    return curDiscount / basePrice * 100;
  }

  // true - если все товары имеют базовую цену, иначе flase
  private checkIsHaveBasePrice(cartLineList: CartLine[]): boolean {
    let flag = true;
    cartLineList.forEach(el => {
      if (!el.product.isConsiderBasePrice) {
        flag = false;
      }
    });
    return flag;
  }

  getCount(): number {
    return this.lines.length;
  }

  addLine(product: ChainProduct, quantity: number) {

    const line = this.lines.find(lineEl => lineEl.product.productId === product.productId);
    if (line !== undefined) {
      line.quantity += quantity;
    } else {
      this.lines.unshift(new CartLine(product, quantity, ''));
    }
    this.recalculate();
  }

  updateQuantity(product: ChainProduct, quantity: number) {
    const line = this.lines.find(lineEl => lineEl.product.productId === product.productId);
    if (line !== undefined) {
      line.quantity = Number(quantity);
    }
    this.recalculate();
  }

  removeLine(id: string) {
    const index: number = this.lines.findIndex(line => line.product.productId === id);
    this.lines.splice(index, 1);
    this.recalculate();
  }

  getCartByChain(chainId: number): CartLine[] {
    const cartListByChain: CartLine[] = [];
    this.lines.map(line => {
      if (line.product.chainId === chainId) {
        cartListByChain.push(line);
      }
    });
    return cartListByChain;
  }
  public checkIsBasePriceByChain(chainId: number): boolean {
    return this.checkIsHaveBasePrice(this.lines.filter( line => {
      return line.product.chainId === chainId;
    }));
  }

  public checkAllIsBasePrice(): boolean {
    return this.isAllHaveBasePrice;
  }

  // цена без учета скидки всех товаров в корзине, где известна начальная цена
  getAllPriceBase() {
    return this.sumAllBasePrice;
  }

  // цена без учета скидки по выбранной сети, где известна начальная цена
  getAllPriceBaseByChain(chainId: number) {
    return this.culcSumBasePrice(this.lines.filter( line => {
      return line.product.chainId === chainId;
    }));
  }

  // стоимость с учетом скидки всех товаров в карзине
  getAllPriceDiscount() {
    return this.sumAllDiscountPrice;
  }

  // стоимость с учетом скидки по выбранной сети
  getAllPriceDiscountByChain(chainId: number) {
    return this.culcSumDiscountPrice(this.lines.filter( line => {
      return line.product.chainId === chainId;
    }));
  }

  // суммарная скдика всех товаров в корзине в деньгах
  getAllDiscountInMoney(): number {
    return this.sumDiscountInMoney;
  }

  // суммарная скдика всех товаров в корзине по выбранной сети в деньгах
  getAllDiscountByChainInMoney(chainId: number): number {
    return this.culcDiscountInMoney(this.lines.filter(line => {
      return line.product.chainId === chainId;
    }));
  }

  // суммарная скдика всех товаров в корзине в процентах
  getAllDiscountInPercent(): number {
    return  this.sumDiscountInPercent;
  }

  // суммарная скдика товаров в корзине по выбранной сети в процентах
  getAllDiscountByChainInPercent(chainId: number): number {
    return this.culcDiscountInPercent(this.lines.filter(line => {
      return line.product.chainId === chainId;
    }));
  }

  getExistListChain(): ChainLine[] {
    const chainListExist: ChainLine[] = [];
    this.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id === line.product.chainId))) {
        chainListExist.push(this.getStorageByID(line.product.chainId));
      }
    });
    return chainListExist;
  }

  getStorageByID(id: number): ChainLine {
    return this.chainLst.lines.find(x => x.chain.id === id);
  }

  // -------------------------------------------------------------------------

  clear() {
    this.lines = [];
    this.sumAllBasePrice = 0;
    this.sumAllDiscountPrice = 0;
    this.sumDiscountInMoney = 0;
    this.sumDiscountInPercent = 0;
  }

  private recalculate() {
    this.sumAllBasePrice = this.culcSumBasePrice(this.lines);
    this.sumAllDiscountPrice = this.culcSumDiscountPrice(this.lines);
    this.sumDiscountInMoney = this.culcDiscountInMoney(this.lines);
    this.sumDiscountInPercent = this.culcDiscountInPercent(this.lines);
    this.isAllHaveBasePrice = this.checkIsHaveBasePrice(this.lines);
    this.sessionStorageService.setCartToSessionStorage(this.lines);
  }
}

export class CartLine {

  constructor(public product: ChainProduct,
              public quantity: number,
              public comment: string) {
  }
  get lineTotal() {
    return this.product.getSumWithDiscount(this.quantity);
  }
}
