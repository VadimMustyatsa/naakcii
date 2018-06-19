'use strict';
var Helper = require('../../helpers/helper'),
    Page = require('./page'),
    ChainComponent = require('../components/chainComponent'),
    CategoryComponent = require('../components/categoryComponent'),
    SubcategoryComponent = require('../components/subcategoryComponent'),
    ProductCardComponent = require('../components/productCardComponent'),
    ShortListComponent = require('../components/shortListComponent');

class CatalogPage extends Page {
    constructor() {
        super();
        this.currentComponent = null;
        this.currentRoot = null;
        this.data = {
            'кнопк(?:а|у) перейти к списку': by.css('#goFoodsBtn')
        };
        this.components = {
            'торговые сети': {
                parentRoot: 'app-foods-storage-list',
                childRoot: 'li.collection-item',
                row: 'li label>span',
                Class: ChainComponent
            },
            'список категорий':{
                parentRoot: 'app-foods-group',
                childRoot: '.item',
                row: '.categoryName',
                Class: CategoryComponent
            },
            'список подкатегорий':{
                parentRoot: 'app-foods-subcategory',
                childRoot: 'li.collection-item',
                row: 'label[for*="Cat"]',
                Class: SubcategoryComponent
            },
            'список акционных товаров': {
                parentRoot: 'app-foods-food-list',
                childRoot: '.foodCard',
                row: '.foodName',
                Class: ProductCardComponent
            },
            'список покупок':{
                parentRoot: 'app-foods-total-items',
                childRoot: 'li:not(.collapsible-header)',
                row: '.foodName[class*="left"]',
                Class: ShortListComponent
            }
        }
        this.helper = new Helper(this.data);
    }

    getComponent(component, text = undefined){
        if(!this.components[component]){
            throw new Error('Component is not found: ' + this.components[component]);
        }
        if(text){
            this.currentRoot = element(by.css(this.components[component].parentRoot)).all(by.css(this.components[component].childRoot)).filter((row) => {
                return row.all(by.css(this.components[component].row)).getText().then((rowText) => {
                    return rowText.indexOf(text) > -1;
                });
            }).first();
        } else {
            this.currentRoot = element(by.css(this.components[component].parentRoot));
        }
        this.currentComponent = new this.components[component].Class(this.currentRoot);
        return this.currentComponent;
    }
}

module.exports = CatalogPage;