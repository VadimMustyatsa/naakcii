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

    async getElementText(name){
        var resultText;
        resultText = (name.indexOf('поле') === -1)? await this.root.all(this.matchElement(name)).getText(): await this.root.all(this.matchElement(name)).getAttribute('value');
        return (name.indexOf('информация') < 0)? resultText: resultText.reduce((str, elem) => {
            return str + elem + ' ';
        }, '');
    }

    async clickElement(name = undefined){
        var elem = (name)? this.root.element(this.matchElement(name)): this.root;
        await browser.executeScript("arguments[0].scrollIntoView(false);", elem.getWebElement());
        return await elem.click();
    }
}

module.exports = ShortListComponent;