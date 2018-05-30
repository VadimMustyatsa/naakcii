'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class ChainComponent extends Component{
    constructor(root){
        super();
        this.root = root;
        this.data = {
            'название|(фильтр|фильтре) торговые сети': by.css('div[class^="collapsible-header"]>div:not(:last-child)'),
            'пункт|торговая_сеть': by.css('li label>span'),
            'средний_%_скидки': by.css('li div[class*="Percent"]>span'),
            'товаров_на_акции': by.css('li div[class*="Goods"]>span'),
            'статус': by.css('li input'),
            'состояние': by.css('div[class^="collapsible-header"]')
        };
        this.helper = new Helper(this.data);
    }
}

module.exports = ChainComponent;