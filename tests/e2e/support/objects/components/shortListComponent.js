'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class ShortListComponent extends Component{
    constructor(root){
        super();
        this.root = root;
        this.data = {
            '(панель|панели) список покупок': by.css('li.collapsible-header:nth-of-type(2) div'),
            'товар|акционный_товар': by.css('.foodName[class*="left"]'),
            'количество': by.css('.amountItem>span'),
            'информация': by.css('div.collapsible-body[style*="display"] div.row>div:not(.deleteFood)'),
            'кнопк(?:а|у) удалить': by.css('div.collapsible-body[style*="display"] div.row>.deleteFood'),
            'кнопк(?:а|у) \\+': by.xpath('.//a/i[text()="add"]'),
            'кнопк(?:а|у) \\-': by.xpath('.//a/i[text()="remove"]'),
            'состояние': by.css('.currentItem'),
            'поле': by.css('div.collapsible-body[style*="display"] #item_comment')
        };
        this.helper = new Helper(this.data);
    }

    getElementText(name){
        if(name.indexOf('поле') === -1) {
            return this.root.all(this.matchElement(name)).getText();
        } else {
            return this.root.all(this.matchElement(name)).getAttribute('value');
        }
    }
}

module.exports = ShortListComponent;