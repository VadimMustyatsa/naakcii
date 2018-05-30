'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class ProductCardComponent extends Component{
    constructor(root){
        super();
        this.root = root;
        this.data = {
            '(панель|панели) список акционных товаров': by.css('div p'),
            'название|карточк(?:а|е)|акционный_товар': by.css('.foodName'),
            'торговая_сеть': by.css('.foodStorage>div:first-of-type'),
            'кнопк(?:а|у) добавить': by.css('a.selectFood.btn'),
            'кнопк(?:а|у) \\+': by.xpath('.//a/i[text()="add"]'),
            'кнопк(?:а|у) \\-': by.xpath('.//a/i[text()="remove"]'),
            'количество': by.css('.amountItem>span')
        };
        this.helper = new Helper(this.data);
    }

}

module.exports = ProductCardComponent;