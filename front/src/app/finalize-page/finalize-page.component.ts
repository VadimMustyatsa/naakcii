
import {Component, ElementRef, OnInit, EventEmitter, ChangeDetectionStrategy} from '@angular/core';
import {Cart, CartLine} from '../shared/cart/cart.model';
import {FoodsStorageService} from '../shared/Storage/foods.storage.service';
import {isUndefined} from 'util';
import {FoodList} from '../shared/foodList/foods.foodList.model';
import {Chain, ChainLine} from '../shared/chain/chain.model';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import {$NBSP} from "@angular/compiler/src/chars";
import {MaterializeAction} from 'angular2-materialize'
import { Title } from '@angular/platform-browser'
import {Router} from "@angular/router";

@Component({
  selector: 'app-finalize-page',
  templateUrl: './finalize-page.component.html',
  styleUrls: ['./finalize-page.component.css'],
  providers: [FoodsStorageService],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FinalizePageComponent implements OnInit {
  chainListExist: ChainLine[] = null;
  widthContainer = 1200;

  params = [
    {
      onOpen: (el) => {
      el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_drop_down';
      },
      onClose: (el) => {
        el.prevObject[0].querySelector('.arrowCollapsibleBold').innerHTML = 'arrow_right';
      }
    }
  ];
  modalActions = new EventEmitter<string|MaterializeAction>();
  openModal() {
    this.modalActions.emit({action:"modal",params:['open']});
  }
  closeModal() {
    this.modalActions.emit({action:"modal",params:['close']});
  }
  constructor(private router: Router ,public  chainLst: Chain,
              private el: ElementRef,
              public cart: Cart, private titleService: Title) {
    pdfMake.vfs = pdfFonts.pdfMake.vfs;
    pdfMake.width = '1400px';
  }

  ngOnInit() {
    this.chainListExist = this.getExistListChain();
    this.titleService.setTitle('Список покупок – naakcii.by');
  }

  getExistListChain() {
    const chainListExist: ChainLine[] = [];
    this.cart.lines.map(line => {
      if (isUndefined(chainListExist.find(x => x.chain.id === line.product.idStrore))) {
        chainListExist.push(this.getStorageByID(line.product.idStrore));
      }
    });
    return chainListExist;
  }

  getCartByChain(idChain: number): CartLine[] {
    const cartListByChain: CartLine[] = [];
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
      if (line.product.idStrore === idChain) {
        quantity += line.quantity;
      }
    });
    return quantity;
  }

  getCartAllPriceByChain(idChain: number) {
    let allPrice = 0;
    this.cart.lines.map(line => {
      if (line.product.idStrore === idChain) {
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
      if (line.product.idStrore === idChain) {
        totalPrice += (line.product.totalPrice * line.quantity);
      }
    });
    return totalPrice;
  }

  getSumDiscount(food: FoodList) {
    return (food.allPrice - food.totalPrice).toFixed(2);
  }

  getStorageByID(id: number): ChainLine {
    console.log(this.chainLst);
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

    const rootElement = this.el.nativeElement;
    const childElement = rootElement.firstElementChild;
    const contentElement = childElement.firstElementChild;
    this.widthContainer = contentElement.clientWidth;
  }

  getPosition() {
    this.onResizeContent();
    const clientHeight = document.documentElement.clientHeight; // высота видимой части
    const offsetHeight = document.documentElement.offsetHeight;
    if ((offsetHeight - clientHeight) < 60) {
      return '';
    } else {
      return 'fixed';
    }
  }

  onRederect(){
    localStorage.clear();
    window.location.href = '/form-shopping-list';
  }
  //*****************************************************************************************
  generatePDF() {
    let data = this.cart.generateJsonListPDF(); //сформированный список по сетям
    console.log(JSON.stringify(data));
    const docDefinition = {};
    const docContent = [];
    const docStyle = {};
    const chainSum = [];
    let sumAfter = '';
    let benefit = '';
    let day = '';
    let year = '';
    let monthString = '';
    const time = new Date();
    const month = time.getMonth() + 1;
    if (time.getDate().toString().length === 1) {
      day = '0' + time.getDate().toString();
    } else {
      day = time.getDate().toString();
    }
    if (time.getMonth().toString().length === 1) {
      monthString = '0' + month.toString();
    }
    year = time.getFullYear().toString().substr(2, 4);

    function numberFormater (number:number): string {
      return number.toString().replace('.',',');
    }

    let totalSum = {};
    totalSum = data['totalSum'];
    sumAfter = numberFormater((totalSum['sumAfter']).toFixed(2)) + ' руб.';
    benefit = numberFormater((totalSum['discountSum']).toFixed(2)) + ' руб. (' + (totalSum['discountPersent']).toFixed(0) + ' %)';

    docContent.push({
      text: 'Список покупок ' + day + '.' + monthString + '.' + year,
      style: 'header'
    });

    for (var chain in data['ChainList']) {
      //заголовок текущей сети
      docContent.push({text: '', margin: [0, 25, 0, 0]}); //пустая строка
      let table = {}; //обрамление
      let bodyTable = {};
      let bodyBodyTable = [];
      let sum = 0;
      let widthParam = [];
      widthParam.push('100%');
      bodyTable['widths'] = widthParam;
      let tableLine = [];
      data['ChainList'][chain].map(item => {
        sum = Number(item['priceSum']) + sum;
      });
      tableLine.push({
        alignment: 'left',
        bold: true,
        fillColor: '#656565',
        color: 'white',
        fontSize: '38',
        columns: [{width: '70%', text: chain}, {width: '30%', text: 'Итого : ' + sum}]
      });
      chainSum.push(sum);
      bodyBodyTable.push(tableLine);
      bodyTable['body'] = bodyBodyTable;
      table['table'] = bodyTable;
      table['layout'] = 'noBorders';
      docContent.push(table);
      //--------------------------------------------------------------

      data['ChainList'][chain].map(item => {  //перебираем товарные позиции
        let itemColumnList = {}; //строка
        let columns = [];
        let itemNameComment = '';
        if (item['Comment'] == '') {
          itemNameComment = item['Name'];
        } else {
          itemNameComment = item['Name'] + '\n' + '(' + item['Comment'] + ')';
        }
        columns.push({width: '60%', text: itemNameComment, margin: [0, 15]});
        columns.push({width: '10%', text: '', margin: [0, 15]});
        columns.push({
          width: '10%',
          text: numberFormater(item['priceOne']),
          style: 'itemSumStyle',
          alignment: 'center',
          margin: [0, 15]
        });
        columns.push({
          width: '10%',
          text: '×' + item['amount'],
          style: 'itemSumStyle',
          alignment: 'center',
          margin: [0, 15]
        });
        columns.push({width: '10%', text: numberFormater(item['priceSum']), style: 'itemSumStyle', margin: [0, 15]});

        itemColumnList['columns'] = columns;
        itemColumnList['style'] = 'itemStyle';
        docContent.push(itemColumnList);
      });
    }
    //итоговая сумма------------------------------
    let itemColumnList = {}; //строка
    let columns = [];

    columns.push({width: '70%', text: 'Общий итог :', bold: true, margin: [0, 30, 0, 10]});
    columns.push({width: '30%', text: sumAfter, bold: true, style: 'itemSumStyle', margin: [0, 30, 0, 10]});

    itemColumnList['columns'] = columns;
    itemColumnList['style'] = 'totalStyle';
    docContent.push(itemColumnList);
    //----------------------------------------

    //Ваша выгода-----------------------------------
    docContent.push({text: 'Экономия :    ' + benefit, bold: true, style: 'totalStyle', margin: [0, 20]});
    //-----------------------------------------------

    //Штампик----------------------------------------
    docContent.push({
      image: 'data:image/png;base64,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',
      width: 280,
      margin: [340, 40, 0, 0]

    });
    //-----------------------------------------------

    let pageSize = {};
    pageSize['width'] = 1000;
    pageSize['height'] = 'auto';

    docDefinition['content'] = docContent;
    docDefinition['pageSize'] = pageSize;
    //docDefinition['pageSize'] = 'A4';
    //стили***************************************************
    let headerStyle = {};
    headerStyle['fontSize'] = 44;
    headerStyle['bold'] = true;
    headerStyle['alignment'] = 'center';

    let itemStyle = {};
    itemStyle['fontSize'] = 32;
    let itemSumStyle = {};
    itemSumStyle['alignment'] = 'right';

    let totalStyle = {};
    totalStyle['fontSize'] = 38;
    //totalStyle['color'] = 'white';
    //totalStyle['background'] = '#656565';

    let anotherStyle = {};
    anotherStyle['italic'] = true;
    anotherStyle['alignment'] = 'right';

    docStyle['header'] = headerStyle;
    docStyle['itemStyle'] = itemStyle;
    docStyle['itemSumStyle'] = itemSumStyle;
    docStyle['totalStyle'] = totalStyle;
    docStyle['anotherStyle'] = anotherStyle;
    docDefinition['styles'] = docStyle;

    pdfMake.createPdf(docDefinition).download('Список покупок - ' + day + '.' + monthString + '.' + year + '.pdf');
    pdfMake.createPdf(docDefinition).open();
    //**********************************************************************
  }
}


