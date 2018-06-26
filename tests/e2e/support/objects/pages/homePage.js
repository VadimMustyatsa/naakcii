'use strict';

var Helper = require('../../helpers/helper'),
    Page = require('./page');

class HomePage extends Page{
    constructor(){
        super();
        this.data = {
            'иллюстрация дескриптор': by.css('app-home-slider>div[class^="homeSlider"]'),
            'схем(?:ы|а)': by.css('div[class="steps-container-block"]'),
            'шага': by.xpath('.//div[@class="steps-container"]//div[@class="step-title"]//span[2]'),
            'кнопк(?:а|у) перейти к товарам': by.css('a#goFoodsBtn'),
            'логотип': by.css('a#logo'),
            'главная': by.css('a[href="/"]')
        };
        this.helper = new Helper(this.data);
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
        return element.all(this.data[strArr[1]]).get(elementIndex).getText();
    }
}

module.exports = HomePage;
