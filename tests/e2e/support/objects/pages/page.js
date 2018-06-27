'use strict';

class Page {
    constructor(){};

    getPageUrl(){
        return browser.driver.getCurrentUrl();
    }

    getPageTitle(){
        return browser.driver.getTitle();
    }

    async scrollPageDown(isScrolledUp = false){
        var isPageScrollingEnd = false;
        while(isPageScrollingEnd === false){
            await browser.executeScript('window.scrollTo(0,document.body.scrollHeight);');
            isPageScrollingEnd = await browser.wait(() =>{
                return browser.executeScript('return ((window.innerHeight + window.scrollY) < (document.body.offsetHeight - 10))').then((res) => {return res;});
            }, 5000).then(() => false, () => true);
        }

        if(isScrolledUp === true){
            await browser.executeScript('window.scrollTo(0,0);');
        }
    }

    clickKeyButton(buttonName = undefined){
        return browser.actions().sendKeys(protractor.Key.END).perform();
    }

    clickElement(elementKey, subElementKey = elementKey) {
        var elementObj = this.helper.getElementLocator(elementKey, subElementKey);
        return element(elementObj).click();
    }

    async isElementDisplayed(elementKey, subElementKey, elementText = undefined) {
        var elementObj = this.helper.getElementLocator(elementKey, subElementKey);
        if(elementText === undefined) {
            return await element(elementObj).isDisplayed();
        } else {
            return ((await element.all(elementObj).map().then((elem) => {return elem.getText();})).indexOf(elementText) > -1)? true: false;
        }
    }

    isElementPresent(elementKey, subElementKey) {
        var elementObj = this.helper.getElementLocator(elementKey, subElementKey);
        return element(elementObj).isPresent();
    }

    getElementsNumber(elementKey, subElementKey = elementKey) {
        var elementObj = this.helper.getElementLocator(elementKey, subElementKey);
        return element.all(elementObj).count();
    }
}

module.exports = Page;