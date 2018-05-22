'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class CategoryComponent extends Component{
    constructor(componentRoot){
        super();
        this.componentRoot = componentRoot;
        this.data = {
            '(панель|панели) список категорий': by.css('div[class$="foods-main-group"]'),
            'категор(?:ия|ию|ии)': by.css('.categoryName'),
            'статус': by.css('.item>div')
        };
        this.helper = new Helper(this.data);
    }
}

module.exports = CategoryComponent;