import {Injectable} from '@angular/core';
import {Chain, ChainLine} from '../chain/chain.model';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import {Cart} from '../cart/cart.model';
import {UndiscountService} from './undiscount.service';
import {stamp} from './stamp-for-pdf';

@Injectable()
export class PdfGeneratorService {
  constructor(public cart: Cart, private undiscountStorage: UndiscountService) {
    pdfMake.vfs = pdfFonts.pdfMake.vfs;
    pdfMake.width = '1400px';
  }

  generatePDF() {
    const data = this.cart.generateJsonListPDF(); // сформированный список по сетям
    let docDefinition = {};
    const docContent = [];
    const docStyle = {};
    const chainSum = [];
    let sumAfter = '';
    let benefit = '';
    const date = new Date().toLocaleString('ru', {day: '2-digit', month: '2-digit', year: '2-digit'});

    function numberFormater(number: number): string {
      return number.toString().replace('.', ',');
    }

    let totalSum = {};
    totalSum = data['totalSum'];
    sumAfter = numberFormater((totalSum['sumAfter']).toFixed(2)) + ' руб.';
    benefit = numberFormater((totalSum['discountSum']).toFixed(2)) + ' руб. (' + (totalSum['discountPersent']).toFixed(0) + ' %)';

    docContent.push({
      text: 'Список покупок ' + date,
      style: 'header'
    });

    for (const chain in data['ChainList']) {
      if (data['ChainList'].hasOwnProperty(chain)) {
        // заголовок текущей сети
        docContent.push({text: '', margin: [0, 25, 0, 0]}); // пустая строка
        let table = {}; // обрамление
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
          fontSize: '16',
          columns: [{width: '70%', text: chain}, {
            width: '30%',
            text: 'Итого : ' + numberFormater(Number(sum.toFixed(2)))
          }]
        });
        chainSum.push(sum);
        bodyBodyTable.push(tableLine);
        bodyTable['body'] = bodyBodyTable;
        table['table'] = bodyTable;
        table['layout'] = 'noBorders';
        docContent.push(table);
        // --------------------------------------------------------------

        data['ChainList'][chain].map(item => {  // перебираем товарные позиции
          let itemColumnList = {}; // строка
          let columns = [];
          let itemNameComment = '';
          if (item['Comment'] === '') {
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
    }
// -------------------------------------------------------------------------
    const table = {}; // обрамление
    const bodyTable = {};
    const bodyBodyTable = [];
    const sum = 0;
    const widthParam = [];
    widthParam.push('100%');
    bodyTable['widths'] = widthParam;
    const tableLine = [];
    tableLine.push({
      alignment: 'left',
      bold: true,
      fillColor: '#656565',
      color: 'white',
      // fontSize: '38',
      fontSize: '16',
      columns: [{width: '70%', text: 'Неакционные товары'}]
    });
    chainSum.push(sum);
    bodyBodyTable.push(tableLine);
    bodyTable['body'] = bodyBodyTable;
    table['table'] = bodyTable;
    // table['layout'] = 'noBorders';
    if (this.undiscountStorage.getFromUndiscount()[0]) {
      docContent.push(table);
    }
// ---------------------------------------------------------------------

//     this.undiscountStorage.getFromUndiscount().map(item => {
//       const itemColumnList = {}; // строка
//       const columns = [];
//       columns.push({
//         width: '70%',
//         text: item.text,
//         style: 'itemSumStyle',
//         alignment: 'left',
//         margin: [0, 15]
//       });

//       itemColumnList['columns'] = columns;
//       itemColumnList['style'] = 'itemStyle';
//       if (this.undiscountStorage.getFromUndiscount()[0]) {
//         docContent.push(itemColumnList);
//       }
//     });

    // итоговая сумма------------------------------
    const itemColumnList = {}; // строка
    const columns = [];

    columns.push({width: '40%', text: 'Общий итог :', bold: true, margin: [0, 30, 0, 10]});
    columns.push({width: '10%', text: sumAfter, bold: true, style: 'itemSumStyle', margin: [0, 30, 0, 10]});

    itemColumnList['columns'] = columns;
    itemColumnList['style'] = 'totalStyle';
    docContent.push(itemColumnList);
    // ----------------------------------------

//     // Ваша выгода-----------------------------------
//     docContent.push({text: 'Экономия :    ' + benefit, bold: true, style: 'totalStyle', margin: [0, 20]});
//     // -----------------------------------------------

    // Штампик----------------------------------------
    // let imm;
    // const img = new Image();
    // img.src = 'assets/images/pdfLogo.png';
    // img.onload = function() {
    //   console.log('onload');
    //   const canvas = document.createElement('canvas');
    //   canvas.width = img.width;
    //   canvas.height = img.height;
    //   const ctx = canvas.getContext('2d');
    //   ctx.drawImage(img, 0, 0);
    //   const dataURL = canvas.toDataURL('image/png');
    //   imm = dataURL;
    //   console.log(dataURL);
    //   // console.log(dataURL.replace(/^data:image\/(png|jpg);base64,/, ''));
    // };
    // docContent.push({
    //   image: stamp,
    //   // image: imm,
    //   width: 280,
    //   margin: [340, 40, 0, 0]

    // });
    // docDefinition['background'] = {
    //   image: stamp,
    //   // image: imm,
    //   width: 280,
    //   margin: [340, 40, 0, 0]

    // };
    // -----------------------------------------------

    // const pageSize = {};
    // pageSize['width'] = 620;
    // pageSize['width'] = 1000;
    // pageSize['height'] = 877;
    // pageSize['height'] = 'auto';
    let tblc = {
    table: {
       widths: ['50%', '50%'],
       body: [[docContent, 'sdf']]
     }
    };
    let cnt = [];
    cnt.push(tblc)
    // docDefinition['content'] = docContent;
    docDefinition = {}
    console.log(docDefinition)
    docDefinition['content'] = cnt;
    console.log(docDefinition)
    // docDefinition['pageSize'] = pageSize;
    docDefinition['pageSize'] = 'A4';
    docDefinition['pageOrientation'] = 'landscape';
    // стили***************************************************
    // const headerStyle = {};
    // // headerStyle['fontSize'] = 44;
    // headerStyle['fontSize'] = 16;
    // headerStyle['bold'] = true;
    // headerStyle['alignment'] = 'center';

    // const itemStyle = {};
    // // itemStyle['fontSize'] = 32;
    // itemStyle['fontSize'] = 14;
    // const itemSumStyle = {};
    // itemSumStyle['alignment'] = 'right';

    // const totalStyle = {};
    // // totalStyle['fontSize'] = 38;
    // totalStyle['fontSize'] = 16;
    // // totalStyle['color'] = 'white';
    // // totalStyle['background'] = '#656565';

    // const anotherStyle = {};
    // anotherStyle['italic'] = true;
    // anotherStyle['alignment'] = 'right';

    // docStyle['header'] = headerStyle;
    // docStyle['itemStyle'] = itemStyle;
    // docStyle['itemSumStyle'] = itemSumStyle;
    // docStyle['totalStyle'] = totalStyle;
    // docStyle['anotherStyle'] = anotherStyle;
    // docDefinition['styles'] = docStyle;

    pdfMake.createPdf(docDefinition).download('Список покупок - ' + date + '.pdf');
    pdfMake.createPdf(docDefinition).open();
    // **********************************************************************
  }
}
