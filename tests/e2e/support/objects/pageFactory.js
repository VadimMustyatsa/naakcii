'use strict'

const Page = require('./pages/page'),
    HomePage = require('./pages/homePage'),
    CatalogPage = require('./pages/catalogPage'),
    ShoppingListPage = require('./pages/shoppingListPage');

class PageFactory{
    constructor(){
        this.currentPage = null;
    }
    getPage(page){
        var pages = {
            'page': Page,
            'сервис экономии – naakcii.by': HomePage,
            'формирование списка покупок – naakcii.by': CatalogPage,
            'список покупок – naakcii.by': ShoppingListPage
        };
        if(!pages[page]){
            throw new Error('Page is not found: ' + pages[page]);
        }
        this.currentPage = new pages[page]();
        return this.currentPage;
    }
}

module.exports = PageFactory;