'use strict';
var world = require('../../support/world');

let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout}) {

    setDefaultTimeout(180000);

    Given(/^я нажимаю на (кнопку|фильтр) "(.+)"$/, async function(partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase();
        return await world.pageFactory.currentPage.clickElement(elementKey.toLocaleLowerCase());
    });

    Given(/^я нажимаю клавишу "(.+)"$/, async function(keyButton) {
        return await world.pageFactory.currentPage.clickPageDownButton();
    });

    Given(/^я прокручиваю страницу до конца$/, async function() {
        return await world.pageFactory.currentPage.scrollPageDown(true);
    });

    Then(/^в адресной строке браузера должен отобразиться адрес "(.+)"$/, async function (pageUrl) {
        var actualPageUrl;
        actualPageUrl = await world.pageFactory.currentPage.getPageUrl() + '/';
        return expect(actualPageUrl).to.equal(pageUrl);
    });

    Then(/^долж(?:ен|на|но) отобразиться (фильтр|иллюстрация|кнопка|поле|панель) "(.+)"$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualResult;
        actualResult = await world.pageFactory.currentPage.isElementDisplayed(elementKey);
        return expect(actualResult).to.equal(true);
    });

});