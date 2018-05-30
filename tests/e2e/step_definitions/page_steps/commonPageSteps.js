'use strict';
var world = require('../../support/world');

let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout}) {

    setDefaultTimeout(180000);

    Given(/^я нажимаю на (кнопку) "(.+)"$/, async function(element, name) {
        var elem = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase();
        return await world.pageFactory.currentPage.clickElement(elem.toLocaleLowerCase());
    });
    Given(/^я нажимаю на (фильтр) "(.+)"$/, async function(element, name) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase();
        return await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).clickElement(component);
    });

    Given(/^я нажимаю клавишу "(.+)"$/, async function(buttonName) {
        return await world.pageFactory.currentPage.clickKeyButton(buttonName);
    });

    Given(/^я прокручиваю страницу до конца$/, async function() {
        return await world.pageFactory.currentPage.scrollPageDown(true);
    });

    Given(/^я нажимаю (кнопку) "(.+)" (\d) (?:раза|раз) (?:на карточке|для товара) "(.+)" на панели "(.+)"$/, async function(button, buttonName, qty, text, name) {
        var bttn = button.toLocaleLowerCase() + ' ' + buttonName.toLocaleLowerCase();
        for(var i = 0; i < qty; i += 1){
            await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).clickElement(bttn);
        }
    });
    Given(/^я нажимаю (кнопку) "(.+)" (?:на карточке|для товара|на товаре) "(.+)" на панели "(.+)"$/, async function(button, buttonName, text, name) {
        var bttn = button.toLocaleLowerCase() + ' ' +  buttonName.toLocaleLowerCase();
        await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).clickElement(bttn);
    });

    Then(/^в адресной строке браузера должен отобразиться адрес "(.+)"$/, async function (pageUrl) {
        var actualPageUrl;
        actualPageUrl = await world.pageFactory.currentPage.getPageUrl() + '/';
        return expect(actualPageUrl).to.equal(pageUrl);
    });

    Then(/^долж(?:ен|на|но) отобразиться (иллюстрация|кнопка) "(.+)"$/, async function (element, name) {
        var elem = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualResult;
        actualResult = await world.pageFactory.currentPage.isElementDisplayed(elem, elem);
        return expect(actualResult).to.equal(true);
    });

    Then(/^долж(?:ен|на|но) отобразиться (фильтр|поле|панель) "(.+)"$/, async function (element, name) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).isElementDisplayed(component);
        return expect(actualResult).to.equal(true);
    });

});