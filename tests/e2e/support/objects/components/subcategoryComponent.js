'use strict';
var Helper = require('../../helpers/helper'),
    Component = require('./component');

class SubcategoryComponent extends Component{
    constructor(componentRoot){
        super();
        //this._state = null; //кэширует текущее состояние - через setter ???
        this.componentRoot = componentRoot;
        this.data = {
            '(панель|панели) список подкатегорий': by.css('ul .categoryName'),
            'пункт|подкатегор(?:ия|ию|ии)': by.css('label[for*="Cat"]'),
            'статус': by.css('input[id*="Cat"]')
        };
        this.helper = new Helper(this.data);
    }
}

module.exports = SubcategoryComponent;