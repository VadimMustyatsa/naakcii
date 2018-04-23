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
                'состояние_фильтра': by.css('app-foods-storage-list div[class^="collapsible-header"]')
            },
            '(панель|панели) список категорий':{
                '(панель|панели) список категорий': by.css('app-foods-group div[class$="foods-main-group"]'),
                'категор(?:ия|ию|ии)': by.css('app-foods-group ngu-item p'),
                'статус': by.css('app-foods-group ngu-item>div')
            },
            '(панель|панели) список подкатегорий':{
                '(панель|панели) список подкатегорий': by.css('app-foods-subcategory ul'),
                'подкатегор(?:ия|ию|ии)': by.css('app-foods-subcategory li label'),
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

    getElementStatusByIndex(elementKey, index){
        var elementObj = this.helper.getElementLocator(elementKey, 'статус'),
            statusArr = element.all(elementObj);
        if(elementKey !== 'панель список категорий') {
            return statusArr.get(index).isSelected()
                .then(function (selected) {
                    if (selected) {
                        return 'выбрана';
                    } else {
                        return 'не выбрана';
                    }
                });
        } else {
            return statusArr.get(index).getAttribute('class')
                .then(function(result){
                    if(result === 'selected'){
                        return 'выбрана';
                    } else {
                        return 'не выбрана';
                    }
                });
        }
    }

    isFilterOpened(elementKey){
        var elementObj = this.helper.getElementLocator(elementKey, 'состояние_фильтра');
        return element(elementObj).getAttribute('class')
            .then(function(result){
                if(result.indexOf('active') !== -1){
                    return true;
                } else {
                    return false;
                }
            });
    }

    async getElementValues(elementKey, elementKeysArr, count = 'undefined'){
        var resultArr = [],
            valuesArrLength, //количество значений элементов в списке elementNameArr
            elementKeysArrLength,//количество элементов, для которых вытягиваются значения
            elementObj;

        if(Array.isArray(elementKeysArr)){
            elementKeysArrLength = elementKeysArr.length;

            if(count === 'undefined') {
                valuesArrLength = await this.getNumberOfElements(elementKey, elementKeysArr[0]);
            }else {
                valuesArrLength = count;
            }
            console.log('Array count = ' + valuesArrLength);

            for(var i = 0; i < valuesArrLength; i += 1){
                var elem = [];
                for(var j = 0; j < elementKeysArrLength; j += 1){
                    if(elementKeysArr[j] !== 'статус') {
                        elem.push(await this.getElementValueByIndex(elementKey, elementKeysArr[j], i));
                    }else {
                        elem.push(await this.getElementStatusByIndex(elementKey, i));
                    }
                }
                resultArr.push(elem);
            }

        } else {
            //elementKey = this.matchElement(elementKey);
            if(count === 'undefined') {
                valuesArrLength = await this.getNumberOfElements(elementKey, elementKeysArr);
            }else {
                valuesArrLength = count;
            }
            elementObj = this.helper.getElementLocator(elementKey, elementKeysArr);
            resultArr.push(await element.all(elementObj)
                .map(function (elements) {
                    return elements.getText();
                })
                .then(function (valuesArr) {
                    var resArr = [];
                    for(var i = 0; i < valuesArrLength; i += 1){
                        resArr.push(valuesArr[i]);
                    }
                    return resArr;
                }));
        }
        return resultArr;
    }

    async selectElement(elementKey, subElementKey, textArr){
        var elementObj = this.helper.getElementLocator(elementKey, subElementKey),
            selected = true,
            textArrLength,
            index;
        if(Array.isArray(textArr)){
            textArrLength = textArr.length;
            for(var i = 0; i < textArrLength; i += 1){
                index = await this.getElementIndex(elementKey, subElementKey, textArr[i]);
                console.log('index = ' + index);

                selected = await this.getElementStatusByIndex(elementKey, index);
                console.log('selected = ' + selected);

                await element.all(elementObj).get(index).click();
            }
        } else {
            index = await this.getElementIndex(elementKey, subElementKey, textArr);
            console.log('index = ' + index);

            selected = await this.getElementStatusByIndex(elementKey, index);
            console.log('selected = ' + selected);

            await element.all(elementObj).get(index).click();
        }
    }
}

module.exports = CatalogPage;