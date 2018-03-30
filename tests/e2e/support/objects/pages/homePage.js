'use strict';
//var {element, by} = require('protractor');
const Page = require('./page');

class HomePage extends Page{
    constructor(){
        super();
        this.data = {
            'дескриптор': by.css('app-home-slider>div[class^="homeSlider"]'),
            'схемы': by.css('app-home-step>div[class="steps-container-block"]'),
            'шага': by.css('div[class="steps-container"]>app-home-step'),
            'кнопка': by.css('a[class$="btn"]'),
            'логотип': by.css('a[class$="brand-logo"]'),
            'главная': by.css('a[href="/"]')
        };
    }

    getStepText(elementName) {
        let strArr = elementName.split(' '),
            elementIndex,
            matches = {
                'первого': 0,
                'второго': 1,
                'третьего': 2,
                'четвертого': 3,
                'пятого': 4
            };

        elementIndex = matches[strArr[0]];
        return element.all(this.data[strArr[1]]).get(elementIndex).getAttribute('ng-reflect-text');
    }
}

module.exports = HomePage;

