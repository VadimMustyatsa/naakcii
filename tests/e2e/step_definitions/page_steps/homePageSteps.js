'use strict';

var HomePage = require('../../support/objects/pages/homePage.js');
var chai = require('chai');
var chaiAsPromised = require('chai-as-promised');

chai.use(chaiAsPromised);
var expect = chai.expect,
    homePage = new HomePage();

let cucumber = require('cucumber');


cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout }) {

    setDefaultTimeout(180000);

    Given(/^я перехожу по ссылке "(.+)"$/, async function(pageUrl) {
        return await homePage.openPageByUrl(pageUrl);
    });
    Given(/^я нажимаю на (.+) в верхней части страницы$/, async function(elementName) {
        return await homePage.clickElement(elementName);
    });

    Then(/^должна открыться "(.+)" страница$/, async function (elementName) {
        var isDisplayed = await homePage.isElementDisplayed(elementName);
        if(isDisplayed) {
            return expect(await homePage.getPageUrl()).to.equal(browser.baseUrl);
        } else {
            return false;
        }
    });
    Then(/^должна отобразиться иллюстрация "(.+)"$/, async function (elementName) {
        var isDisplayed = await homePage.isElementDisplayed(elementName);
        return expect(isDisplayed).to.equal(true);
    });
    Then(/^должно отобразиться "(\d)" иллюстраций пояснительной (.+)$/, async function (expNumber, elementName) {
        var isDisplayed = await homePage.isElementDisplayed(elementName),
            actNumber = await homePage.getNumberOfElements(elementName);
        if(isDisplayed){
            return expect(actNumber).to.equal(Number(expNumber));
        } else {
            return false;
        }
    });
    Then(/^должен отобразиться текст (.+) "(.+)"$/, async function (elementName, expText) {
        var actText = await homePage.getStepText(elementName);
        return expect(actText).to.equal(expText);
    });

    Then(/^должна отобразиться (кнопка) "(.+)"$/, async function (elementName, expText) {
        var isDisplayed = await homePage.isElementDisplayed(elementName),
            actText = await homePage.getElementText(elementName);
        if(isDisplayed){
            return expect(actText.toLocaleLowerCase()).to.equal(expText.toLocaleLowerCase());
        } else {
            return false;
        }
    });

});
