'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class CategoryComponent extends Component{
    constructor(root){
        super();
        this.root = root;
        this.data = {
            '(панель|панели) список категорий': by.css('div[class$="foods-main-group"]'),
            'категор(?:ия|ию|ии)': by.css('.categoryName'),
            'статус': by.css('.item>div')
        };
        this.helper = new Helper(this.data);
    }
    getStatus(name){
        return this.root.all(this.matchElement(name)).map((elem) => {
            return elem.getAttribute('class').then((attributeValue) => {
                return (attributeValue === 'selected')? 'Выбрана': 'Не выбрана';
            });
        });
    }
}

module.exports = CategoryComponent;