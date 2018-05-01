'use strict';

var world = require('../../support/world');
let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout }) {

    setDefaultTimeout(180000);

    Given(/^я нажимаю на (логотип)$/, async function(elementKey) {
        return await world.pageFactory.currentPage.clickElement(elementKey.toLocaleLowerCase());
    });

    Then(/^долж(?:но|ны) отобразиться "(\d)" иллюстраций шагов пояснительной (.+)$/, async function (expectedNumber, elementKey) {
        var actualNumber = await world.pageFactory.currentPage.getNumberOfElements(elementKey.toLocaleLowerCase());
        return expect(actualNumber).to.equal(Number(expectedNumber));
    });

    Then(/^должен отобразиться текст (.+) "(.+)"$/, async function (elementKey, elementText) {
        var actualText = await world.pageFactory.currentPage.getStepText(elementKey.toLocaleLowerCase()),
            expectedText = elementText.substring(3);
        return expect(actualText.toLocaleLowerCase()).to.equal(expectedText.toLocaleLowerCase());
    });

});
