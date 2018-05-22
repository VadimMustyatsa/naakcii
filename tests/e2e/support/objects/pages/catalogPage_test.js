'use strict';
var Helper = require('../../helpers/helper'),
    Page = require('./page'),
    ChainComponent = require('../components/chainComponent'),
    CategoryComponent = require('../components/categoryComponent'),
    SubcategoryComponent = require('../components/subcategoryComponent'),
    ProductCardComponent = require('../components/productCardComponent'),
    ShortListComponent = require('../components/shortListComponent');

class CatalogPage_test extends Page {
    constructor() {
        super();
        this.currentComponent = null;
        this.currentRow = null;
        this.data = {
            'кнопк(?:а|у) перейти к списку': by.css('#goFoodsBtn')
        };
        this.components = {
            'торговые сети': {
                root: 'app-foods-storage-list',
                row: 'li label>span',
                Class: ChainComponent
            },
            'список категорий':{
                root: 'app-foods-group',
                row: '.categoryName',
                Class: CategoryComponent
            },
            'список подкатегорий':{
                root: 'app-foods-subcategory',
                row: 'label[for*="Cat"]',
                Class: SubcategoryComponent
            },
            'список акционных товаров': {
                root: 'app-foods-food-list',
                row: '.foodName',
                Class: ProductCardComponent
            },
            'список покупок':{
                root: 'app-foods-total-items',
                row: '.foodName[class*="left"]',
                Class: ShortListComponent
            }
        }
        this.helper = new Helper(this.data);
    }

    getComponent(component){
        if(!this.components[component]){
            throw new Error('Component is not found: ' + this.components[component]);
        }
        this.currentComponent = new this.components[component].Class(element.all(by.css(this.components[component].root)));
        return this.currentComponent;
    }

    getRow(component, name){
        if(!this.components[component]){
            throw new Error('Component is not found: ' + this.components[component]);
        }
        this.currentRow = element(this.components[component].root).all(this.components[component].row).filter((row) => {
            return row.getText().then((rowText) => {
                return rowText.indexOf(name);
            });
        });
        return new this.components[component].Class(this.currentRow);
    }

}

module.exports = CatalogPage_test;