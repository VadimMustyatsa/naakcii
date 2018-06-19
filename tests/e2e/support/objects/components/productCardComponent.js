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

    async clickElement(name = undefined){
        var elem = (name)? this.root.element(this.matchElement(name)): this.root,
            isPres, theEnd = false;
        isPres = await elem.isPresent();
        if(isPres) {
            await browser.executeScript("arguments[0].scrollIntoView(false);", elem.getWebElement());
        } else {
            while(!isPres || theEnd){
                await browser.executeScript('window.scrollTo(0,document.body.scrollHeight);');
                isPres = await elem.isPresent();
                theEnd = await browser.executeScript('return ((window.innerHeight + window.scrollY) < (document.body.offsetHeight - 10))')
            }
        }
        return await elem.click();
    }
}

module.exports = ProductCardComponent;