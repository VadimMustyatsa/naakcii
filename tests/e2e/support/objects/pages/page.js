'use strict';
var Helper = require('../../helpers/helper');

class Page {
    constructor(){
        this.helper = new Helper();
    };

    getPageUrl(){
        return browser.driver.getCurrentUrl();
    }

    getPageTitle(){
        return browser.driver.getTitle();
    }

    scrollPageDown(){
        return browser.executeScript('window.scrollTo(0,document.body.scrollHeight);');
    }

    clickPageDownButton(){
        return browser.actions().sendKeys(protractor.Key.PAGE_DOWN).perform();
    }

    isElementDisplayed(elementKey, subElementKey = elementKey) {
        var elementObj = this.helper.getElementLocator(this.data, elementKey, subElementKey);
        return element(elementObj).isDisplayed();
    }

    getElementText(elementKey, subElementKey = elementKey) {
        var elementObj = this.helper.getElementLocator(this.data, elementKey, subElementKey);
        return element.all(elementObj).map(function (elements) {
            return elements.getText();
        }).then(function (textArr) {
            var textRes = '';
            for(var i = 0; i < textArr.length; i += 1){
                textRes += textArr[i] + ' ';
            }
            return textRes.trim();
        });
    }

    clickElement(elementKey, subElementKey = elementKey) {
        var elementObj = this.helper.getElementLocator(this.data, elementKey, subElementKey);
        return element(elementObj).click();
    }

    getNumberOfElements(elementKey, subElementKey = elementKey) {
        var elementObj = this.helper.getElementLocator(this.data, elementKey, subElementKey);
        return element.all(elementObj).count();
    }

    getElementValueByIndex(elementKey, subElementKey, index){
        var elementObj = this.helper.getElementLocator(this.data, elementKey, subElementKey);
        return element.all(elementObj).get(index).getText();
    }

    getElementIndex(elementKey, subElementKey, textValue){
        var elementObj = this.helper.getElementLocator(this.data, elementKey, subElementKey);
        if(Array.isArray(textValue)){
            textValue = textValue[0];
        }
        return element.all(elementObj)
            .map(function (elements) {
                return elements.getText();
            })
            .then(function (elementsArr) {
                var count = elementsArr.length,
                    index = -1;
                for(var i = 0; i < count; i += 1){
                    if(elementsArr[i] === textValue) {
                        index = i;
                        i = count;
                    }
                }
                return index;
            });
    }
}

module.exports = Page;