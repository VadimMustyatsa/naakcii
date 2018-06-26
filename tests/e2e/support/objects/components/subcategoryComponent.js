'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class SubcategoryComponent extends Component{
    constructor(root){
        super();
        this.root = root;
        this.data = {
            '(панель|панели) список подкатегорий': by.css('ul .categoryName'),
            'пункт|подкатегор(?:ия|ию|ии)': by.css('a'),
            'статус': by.css('a')
        };
        this.helper = new Helper(this.data);
    }

    getStatus(name){
        return this.root.all(this.matchElement(name)).map((elem) => {
            return elem.getAttribute('class').then((attributeValue) => {
                return (attributeValue.indexOf('active') > -1)? 'Выбрана' : 'Не выбрана';
            });
        });
    }
}

module.exports = SubcategoryComponent;