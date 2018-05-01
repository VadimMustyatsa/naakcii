'use strict';

var world = require('../../support/world');
let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout }) {

    setDefaultTimeout(180000);

    Given(/^я (?:нажимаю на|выбираю) (пункт|категорию) "(.+)" (?:на|в) (панели|фильтре) "(.+)"$/, async function(subElementName, selectedText, partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase();
        return await world.pageFactory.currentPage.selectElement(elementKey, subElementName, selectedText);
    });

    Given(/^я выбираю следующие (?:торговые сети в|подкатегории на) (фильтре|панели) "(.+)":$/, async function(partElementKey1, partElementKey2, dataTable) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            subElementKey = dataTable.raw()[0], data = dataTable.raw();
        data.shift();
        if(Array.isArray(subElementKey)){
            subElementKey = subElementKey[0];
        }
        return await world.pageFactory.currentPage.selectElement(elementKey, subElementKey , data);
    });

    Given(/^на (фильтре) "(.+)" отображается текст "(.+)"$/, async function (partElementKey1, partElementKey2, expectedText) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getElementText(elementKey);
        return expect(actualText).to.equal(expectedText);
    });

    Given(/^(фильтр) "(.+)" раскрыт$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        await world.pageFactory.currentPage.clickElement(elementKey.toLocaleLowerCase());
        actualRes = await world.pageFactory.currentPage.isFilterOpened(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(true);
    });

    Given(/^(?:на|в) (панели|фильтре) "(.+)" (?:отображаются|выбраны) (?:.+):$/, async function (partElementKey1, partElementKey2, dataTable) {
        var expectedArr = dataTable.raw(),
            elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualArr = [];
        actualArr = await world.pageFactory.currentPage.getElementValues(elementKey.toLocaleLowerCase(), expectedArr[0], expectedArr.length - 1);
        expectedArr.shift();
        return expect(actualArr).to.deep.equal(expectedArr);
    });

    Given(/^на (панели) "(.+)" не отображается ни одной карточки$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        actualRes = await world.pageFactory.currentPage.getNumberOfElements(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(Number(1));
    });

    Then(/^на (фильтре) "(.+)" должен отобразиться текст "(.+)"$/, async function (partElementKey1, partElementKey2, expectedText) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getElementText(elementKey);
        return expect(actualText).to.equal(expectedText);
    });

    Then(/^на (панели) "(.+)" должен (?:отобразиться|появиться) следующий текст:$/, async function (partElementKey1, partElementKey2, text) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualText,expectedText;
        expectedText = text.replace(/\n/g,'').replace(/, /g,',').replace(/\. /g,'.');
        actualText = (await world.pageFactory.currentPage.getElementText(elementKey)).replace(/\n/g,'').replace(/, /g,',').replace(/\. /g,'.');
        return expect(actualText).to.equal(expectedText);
    });

    Then(/^(?:на|в) (панели|фильтре) "(.+)" должны отобразиться (?:.+):$/, async function (partElementKey1, partElementKey2, dataTable) {
        var expectedArr = dataTable.raw(),
            elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualArr = [];
        actualArr = await world.pageFactory.currentPage.getElementValues(elementKey.toLocaleLowerCase(), expectedArr[0], expectedArr.length - 1);
        expectedArr.shift();
        if(partElementKey1.indexOf('фильтр') > -1){
            await world.pageFactory.currentPage.clickElement(elementKey.toLocaleLowerCase());
        }
        return expect(actualArr).to.deep.equal(expectedArr);
    });

    Then(/^на (панели) "(.+)" должны плавно подгрузиться (?:.+):$/, async function (partElementKey1, partElementKey2, dataTable) {
        var expectedArr = dataTable.raw(),
            elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualArr = [];
        actualArr = await world.pageFactory.currentPage.getElementValues(elementKey.toLocaleLowerCase(), expectedArr[0], expectedArr.length -1);
        expectedArr.shift();
        return expect(actualArr).to.deep.equal(expectedArr);
    });

    Then(/^(фильтр) "(.+)" должен раскрыться$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        actualRes = await world.pageFactory.currentPage.isFilterOpened(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(true);
    });

    Then(/^на (панели) "(.+)" не должно остаться ни одной карточки$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        actualRes = await world.pageFactory.currentPage.getNumberOfElements(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(Number(1));
    });
});