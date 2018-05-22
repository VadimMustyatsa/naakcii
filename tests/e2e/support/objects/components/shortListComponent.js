'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class ShortListComponent extends Component{
    constructor(componentRoot){
        super();
        this.componentRoot = componentRoot;
        this.data = {
            '(панель|панели) список покупок': by.css('div li:last-child>div'),
            'текст': by.css('li.totalSum span'),
            'товар|акционный_товар': by.css('.foodName[class*="left"]'),
            'количество': by.css('.amountItem>span'),
            'информация': by.css('li.active>div.collapsible-body div.row>div:not(.deleteFood)'),// ???
            'кнопк(?:а|у) удалить': by.css('li.active>div.collapsible-body div.row>.deleteFood'),// ???
            'кнопк(?:а|у) \\+': by.xpath('.//li//a/i[text()="add"]'),
            'кнопк(?:а|у) \\-': by.xpath('.//li//a/i[text()="remove"]'),
            'состояние': by.css('.currentItem')
        };
        this.helper = new Helper(this.data);
    }
}

module.exports = ShortListComponent;