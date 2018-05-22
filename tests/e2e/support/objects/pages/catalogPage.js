'use strict';

var Helper = require('../../helpers/helper'),
    Page = require('./page');

class CatalogPage extends Page{
    constructor(){
        super();
        this.data = {
            '(фильтр|фильтре) торговые сети': {
                '(фильтр|фильтре) торговые сети': by.css('app-foods-storage-list div[class^="collapsible-header"]>div:not(:last-child)'),
                'пункт|торговая_сеть': by.css('app-foods-storage-list li label>span'),
                'средний_%_скидки': by.css('app-foods-storage-list li div[class*="Percent"]>span'),
                'товаров_на_акции': by.css('app-foods-storage-list li div[class*="Goods"]>span'),
                'статус': by.css('app-foods-storage-list li input'),
                'состояние': by.css('app-foods-storage-list div[class^="collapsible-header"]')
            },
            '(панель|панели) список категорий':{
                '(панель|панели) список категорий': by.css('app-foods-group div[class$="foods-main-group"]'),
                'категор(?:ия|ию|ии)': by.css('app-foods-group ngu-item p'),
                'статус': by.css('app-foods-group ngu-item>div')
            },
            '(панель|панели) список подкатегорий':{
                '(панель|панели) список подкатегорий': by.css('app-foods-subcategory ul .categoryName'),
                'пункт|подкатегор(?:ия|ию|ии)': by.css('app-foods-subcategory li label'),
                'статус': by.css('app-foods-subcategory li input')
            },
            '(панель|панели) список акционных товаров': {
                '(панель|панели) список акционных товаров': by.css('app-foods-food-list>div div'),
                'акционный_товар': by.css('app-foods-food-card div[class^="foodName"]'),
                'торговая_сеть': by.css('app-foods-food-card div[class^="foodStorage"]>div:first-of-type'),
                'кнопк(?:а|у) добавить': by.css('app-foods-food-card a.selectFood.btn')
            },
            '(панель|панели) список покупок': by.css('app-foods-total-items div li:last-child>div')
        };
        this.helper = new Helper(this.data);
    }

    getElementStatus(elementKey){
        var elementObj = this.helper.getElementLocator(elementKey, 'статус');
        if(elementKey.search(/панел(?:ь|и) список категорий/gi) === -1) {
            return element.all(elementObj).map((elem) => {
                return elem.isSelected().then((attributeValue) => {
                    if (attributeValue) {
                        return 'Выбрана';
                    } else {
                        return 'Не выбрана';
                    }
                });
            });
        } else {
            return element.all(elementObj).map((elem) => {
                return elem.getAttribute('class').then((attributeValue) => {
                    if (attributeValue === 'selected') {
                        return 'Выбрана';
                    } else {
                        return 'Не выбрана';
                    }
                });
            });
        }
    }

    async getElementText(elementKey, subElementKey){
        var elementObj = this.helper.getElementLocator(elementKey, subElementKey),
            resultArray;

        if(subElementKey === 'статус') {
            resultArray = (await this.getElementStatus(elementKey)).slice();
        }else {
            resultArray = (await element.all(elementObj).map(function (elementArr) {
                return elementArr.getText().then((elementText) => {
                    return elementText.replace(/\\['"$.+*|()?]/g,'');
                });
            }));
        }
        return resultArray;
    }

    async clickElementByText(elementKey, subElementKey, text, btnName = undefined){
        var textArray = [];
        if(Array.isArray(text)){
            textArray = text.slice();
        } else {
            textArray.push(text);
        }
        for(var i = 0; i < textArray.length; i += 1){
            await element.all(this.helper.getElementLocator(elementKey, subElementKey)).getText()
                .then((textArr) => {
                    return textArr.some((elementText, index) => {
                        if (elementText.indexOf(textArray[i]) > -1) {
                            return (btnName === undefined) ? element.all(this.helper.getElementLocator(elementKey, subElementKey)).get(index).click()
                                : element.all(this.helper.getElementLocator(elementKey, btnName)).get(index).click();
                        }
                    });
                });
        }
    }
}

module.exports = CatalogPage;