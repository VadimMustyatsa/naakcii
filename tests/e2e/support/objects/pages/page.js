'use strict';

class Page {
    constructor(){};

    openPageByUrl(url){
        return browser.get(url);
    }

    getPageUrl(){
        return browser.driver.getCurrentUrl();
    }

    isElementDisplayed(elementName) {
        return element(this.data[elementName.toLocaleLowerCase()]).isDisplayed();
    }

    getElementText(elementName) {
        return element(this.data[elementName.toLocaleLowerCase()]).getText();
    }

    clickElement(elementName) {
        return element(this.data[elementName.toLocaleLowerCase()]).click();
    }

    getNumberOfElements(elementName) {
        return element.all(this.data[elementName.toLocaleLowerCase()]).count();

    }

    test() {
        console.log('Parent method');
    }
}

module.exports = Page;