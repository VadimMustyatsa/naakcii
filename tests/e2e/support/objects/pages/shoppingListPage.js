'use strict';
var Helper = require('../../helpers/helper'),
    Page = require('./page'),
    ShoppingListComponent = require('../components/shoppingListComponent');

class ShoppingListPage extends Page {
    constructor() {
        super();
        this.currentComponent = null;
        this.currentRoot = null;
        this.data = {
            'строк(?:а|е) итого': by.css('.finalizeFooter div'),
            'кнопк(?:а|у) скачать pdf': by.css('.finalizeFooterButton a:not(.btnLinkFoods)'),
            'кнопк(?:а|у) вернуться к товарам': by.css('a.btnLinkFoods')
        };
        this.components = {
            'список покупок':{
                parentRoot: 'li:not(.finalizeHeader):not(.finalizeFooterButton)',//li:not(.collapsible-header)
                childRoot: '.chainLogoName>div:last-child',
                rows: 'div.itemLine',
                row: 'p.itemName',
                Class: ShoppingListComponent
            }
        };
        this.helper = new Helper(this.data);
    }

    getComponent(component, text = undefined){
        if(!this.components[component]){
            throw new Error('Component is not found: ' + this.components[component]);
        }
        if(text){
            this.currentRoot = (element.all(by.css(this.components[component].parentRoot)).filter((row) => {
                return row.all(by.css(this.components[component].childRoot)).getText().then((rowText) => {
                    return rowText.indexOf(text) > -1;
                });
            }).first()).all(by.css(this.components[component].rows));
        } else {
            this.currentRoot = element.all(by.css(this.components[component].parentRoot));
        }
        this.currentComponent = new this.components[component].Class(this.currentRoot);
        return this.currentComponent;
    }

    getElement(component, text = undefined){
        if(!this.components[component]){
            throw new Error('Component is not found: ' + this.components[component]);
        }
        if(text){
            this.currentRoot = element.all(by.css(this.components[component].parentRoot)).all(by.css(this.components[component].rows)).filter((row) => {
                return row.all(by.css(this.components[component].row)).getText().then((rowText) => {
                    return rowText.indexOf(text) > -1;
                });
            }).first();
        } else {
            this.currentRoot = element.all(by.css(this.components[component].rows));
        }
        this.currentComponent = new this.components[component].Class(this.currentRoot);
        return this.currentComponent;
    }

    async clickElement(elementKey, subElementKey = elementKey) {
        var elem = element(this.helper.getElementLocator(elementKey, subElementKey));
        await browser.executeScript('window.scrollTo(0,document.body.scrollHeight);');
        return elem.click();
    }
}

module.exports = ShoppingListPage;