'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class ShoppingListComponent extends Component{
    constructor(root){
        super();
        this.root = root;
        this.data = {
            '(панель|панели) список покупок': by.css('.finalizePageName'),
            'торговая_сеть': by.css('div.chainList .chainLogoName>div:last-child'),
            'количество': by.css('div.chainList div.col:nth-child(3)'),
            'товарные_позиции': by.css('div.chainList div.col:nth-child(2)'),
            'стоимость_без_скидки': by.css('div.chainList div.col:nth-child(4) span'),
            'скидка_руб.\\(%\\)|скидка_руб._\\(%\\)': by.css('div.chainList div.col:nth-child(5) span'),
            'стоимость_со_скидкой': by.css('div.chainList div.col:nth-child(6)'),
            'дата_окончания_акции': by.css('div.chainList div.col:nth-child(7)'),

            'акционный_товар': {
                'товар|акционный_товар': by.css('p.itemName'),
                'количество': by.css('div.col:nth-child(3) span'),
                'стоимость_без_скидки': by.css('div.col:nth-child(4) span'),
                'скидка_руб._\\(%\\)|скидка_руб.\\(%\\)': by.css('div.col:nth-child(5) span'),
                'стоимость_со_скидкой': by.css('div.col:nth-child(6)'),
                'дата_окончания_акции': by.css('div.col:nth-child(7)'),

                'кнопк(?:а|у) удалить': by.css('.deleteCartLine'),
                'кнопк(?:а|у) \\+': by.xpath('.//a/i[text()="add"]'),
                'кнопк(?:а|у) \\-': by.xpath('.//a/i[text()="remove"]'),
                'поле|комментарий': by.css('.itemComment>input')
            },
            'итого': {
                'текст': by.css('li.finalizeFooter div.col:nth-child(1)'),
                'товарные_позиции': by.css('li.finalizeFooter div.col:nth-child(2)'),
                'количество': by.css('li.finalizeFooter div.col:nth-child(3)'),
                'стоимость_без_скидки': by.css('li.finalizeFooter div.col:nth-child(4) span'),
                'скидка_руб.\\(%\\)|скидка_руб._\\(%\\)': by.css('li.finalizeFooter div.col:nth-child(5) span'),
                'стоимость_со_скидкой': by.css('li.finalizeFooter div.col:nth-child(6)'),
            }
        };
        this.helper = new Helper(this.data);
    }

    async getElementText(name, row = undefined){
        var elem,
            txt;
        elem = (row)? this.helper.getElementLocator(row, name): this.matchElement(name);
        txt = (name.indexOf('комментарий') === -1)? await this.root.all(elem).getText(): await this.root.all(elem).getAttribute('ng-reflect-model');
        return txt.reduce((str, elem) => {
            return str + elem + ' ';
        }, '');
    }

    clickElement(name, element){
        var key = this.helper.matchElementKey(this.data[element], name);
        return this.root.element(this.data[element][key]).click();
    }

    enterText(name, text, element = 'акционный_товар'){
        var key = this.helper.matchElementKey(this.data[element], name);
        return this.root.element(this.data[element][key]).sendKeys(text);
    }
}

module.exports = ShoppingListComponent;