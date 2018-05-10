'use strict';
var world = require('../support/world');

let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout}) {

    setDefaultTimeout(180000);

    Given(/^я перехожу по ссылке "(.+)"$/, async function(pageUrl) {
        return await browser.get(pageUrl);
    });

    Given(/^я на странице "(.+)"$/, async function(expectedPageName) {
        var actualPageName;
        actualPageName = await world.pageFactory.currentPage.getPageTitle();
        return expect(actualPageName.toLocaleLowerCase()).to.equal(expectedPageName.toLocaleLowerCase());
    });

    Then(/^должна открыться страница "(.+)"$/, async function (expectedPageName) {
        var actualPageName;
        await world.pageFactory.getPage(expectedPageName.toLocaleLowerCase());
        actualPageName = await world.pageFactory.currentPage.getPageTitle();
        return expect(actualPageName.toLocaleLowerCase()).to.equal(expectedPageName.toLocaleLowerCase());
    });

});