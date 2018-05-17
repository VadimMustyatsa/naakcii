import {Component, ElementRef, OnInit} from '@angular/core';
import {Cart, CartLine} from '../shared/cart/cart.model';
import {FoodsStorageService} from '../shared/Storage/foods.storage.service';
import {isUndefined} from 'util';
import {FoodList} from '../shared/foodList/foods.foodList.model';
import {Chain, ChainLine} from '../shared/chain/chain.model';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';

@Component({
  selector: 'app-finalize-page',
  templateUrl: './finalize-page.component.html',
  styleUrls: ['./finalize-page.component.css'],
  providers: [FoodsStorageService]
})
export class FinalizePageComponent implements OnInit {
  chainListExist: ChainLine[] = null;
  widthContainer = 1200;

  constructor(public  chainLst: Chain,
              private el: ElementRef,
              public cart: Cart) {
    pdfMake.vfs = pdfFonts.pdfMake.vfs;
  }

  ngOnInit() {
    this.chainListExist = this.getExistListChain();
  }

  getExistListChain() {
    let chainListExist: ChainLine[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id == line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });
    return chainListExist;
  }

  getCartByChain(idChain: number): CartLine[] {
    let cartListByChain: CartLine[] = [];
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        cartListByChain.push(line);
      }
    });
    return cartListByChain;
  }

  getCartQuantityByChain(idChain: number) {
    let quantity = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        quantity += line.quantity;
      }
    });
    return quantity;
  }

  getCartAllPriceByChain(idChain: number) {
    let allPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        if (line.product.allPrice > 0) {
          allPrice += (line.product.allPrice * line.quantity);
        } else {
          allPrice += (line.product.totalPrice * line.quantity);
        }
      }
    });
    return allPrice;
  }

  getCartTotalPriceByChain(idChain: number) {
    let totalPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore == idChain) {
        totalPrice += (line.product.totalPrice * line.quantity);
      }
    });
    return totalPrice;
  }

  getSumDiscount(food: FoodList) {
    return (food.allPrice - food.totalPrice).toFixed(2);
  }

  getStorageByID(id: number): ChainLine {
    return this.chainLst.lines.find(x => x.chain.id === id);
  }

  getNameStorage(id: number): String {
    if (this.getStorageByID(id)) {
      return this.getStorageByID(id).chain.name;
    }
    return 'unknown';
  }

  deleteCartLine(cartLine: CartLine) {
    this.cart.removeLine(cartLine.product.id);
    this.chainListExist = this.getExistListChain();
  }

  subItem(curFood: CartLine) {
    if (curFood.quantity > 1) {
      this.cart.updateQuantity(curFood.product, Number(curFood.quantity - 1));
    }
  }

  addItem(curFood: CartLine) {
    this.cart.updateQuantity(curFood.product, Number(curFood.quantity + 1));
  }

  onResizeContent() {
    var rootElement = this.el.nativeElement;
    var childElement = rootElement.firstElementChild;
    var contentElement = childElement.firstElementChild;
    this.widthContainer = contentElement.clientWidth;
  }

  getPosition() {
    this.onResizeContent();
    let clientHeight = document.documentElement.clientHeight; //высота видимой части
    let offsetHeight = document.documentElement.offsetHeight;
    if ((offsetHeight - clientHeight) < 60) {
      //this.positionTotalSum = "";
      return "";
    } else {
      //this.positionTotalSum = "fixed";
      return "fixed";
    }
  }

  //*****************************************************************************************
  generatePDF() {
    let data = this.cart.generateJsonListPDF(); //сформированный список по сетям
    console.log(JSON.stringify(data));
    let docDefinition = {};
    let docContent = [];
    let docStyle = {};
    let sumAfter = 0;
    let benefit = '';

    let totalSum = {};
    totalSum = data['totalSum'];
    sumAfter = totalSum['sumAfter'];
    benefit = (totalSum['discountSum']).toFixed(2) + ' руб. (' + (totalSum['discountPersent']).toFixed(0) + ' %)';


    docContent.push({text: 'Список покупок', style: 'header'});

    for (var chain in data['ChainList']) {
      docContent.push({text: chain, style: 'chainStyle', margin: [0, 20]});  //заголовок текущей сети
      data['ChainList'][chain].map(item => {  //перебираем товарные позиции
        let itemColumnList = {}; //строка
        let columns = [];
        columns.push({width: '60%', text: item['Name'], margin: [0, 10]});
        columns.push({width: '10%', text: '', margin: [0, 10]});
        columns.push({width: '10%', text: item['priceOne'], style: 'itemSumStyle', margin: [0, 10]});
        columns.push({width: '10%', text: '*' + item['amount'], style: 'itemSumStyle', margin: [0, 10]});
        columns.push({width: '10%', text: item['priceSum'], style: 'itemSumStyle', margin: [0, 10]});

        itemColumnList['columns'] = columns;
        itemColumnList['style'] = 'itemStyle';
        docContent.push(itemColumnList);
      });
    }
    //итоговая сумма---------------------------
    let itemColumnList = {}; //строка
    let columns = [];

    columns.push({width: '70%', text: 'Итого:', margin: [0, 30, 0, 10]});
    columns.push({width: '30%', text: (sumAfter).toFixed(2), style: 'itemSumStyle', margin: [0, 30, 0, 10]});

    itemColumnList['columns'] = columns;
    itemColumnList['style'] = 'totalStyle';
    docContent.push(itemColumnList);
    //----------------------------------------

    //Ваша выгода---------------------------
    itemColumnList = {}; //строка
    columns = [];
    columns.push({width: '70%', text: 'Ваша выгода:', margin: [0, 30, 0, 10]});
    columns.push({width: '30%', text: benefit, style: 'itemSumStyle', margin: [0, 30, 0, 10]});

    itemColumnList['columns'] = columns;
    itemColumnList['style'] = 'totalStyle';
    docContent.push(itemColumnList);
    //----------------------------------------

    docDefinition['content'] = docContent;
    //стили********************************************************
    let headerStyle = {};
    headerStyle['fontSize'] = 22;
    headerStyle['bold'] = true;
    headerStyle['alignment'] = 'center';

    let chainStyle = {};
    chainStyle['fontSize'] = 18;
    chainStyle['width'] = '100%';
    chainStyle['bold'] = true;
    chainStyle['alignment'] = 'center';
    chainStyle['color'] = 'white';
    chainStyle['background'] = '#656565';

    let itemStyle = {};
    let itemSumStyle = {};
    itemSumStyle['alignment'] = 'right';

    let totalStyle = {};
    totalStyle['fontSize'] = 18;
    totalStyle['color'] = 'white';
    totalStyle['background'] = '#656565';

    let anotherStyle = {};
    anotherStyle['italic'] = true;
    anotherStyle['alignment'] = 'right';

    docStyle['header'] = headerStyle;
    docStyle['chainStyle'] = chainStyle;
    docStyle['itemSumStyle'] = itemSumStyle;
    docStyle['totalStyle'] = totalStyle;
    docStyle['anotherStyle'] = anotherStyle;
    docDefinition['styles'] = docStyle;
    //**************************************************************

    // pdfMake.createPdf(docDefinition).download();
    pdfMake.createPdf(docDefinition).open();
  }
}
