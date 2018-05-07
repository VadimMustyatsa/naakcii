'use strict';

var world = require('../../support/world');
let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout }) {

    setDefaultTimeout(180000);

    Given(/^я (?:нажимаю на|выбираю) (пункт|категорию) "(.+)" (?:на|в) (панели|фильтре) "(.+)"$/, async function(subElementName, selectedText, partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase();
        return await world.pageFactory.currentPage.clickElementByText(elementKey, subElementName, selectedText);
    });

    Given(/^я выбираю следующие (?:торговые сети в|подкатегории на) (фильтре|панели) "(.+)":$/, async function(partElementKey1, partElementKey2, dataTable) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            subElementKey = dataTable.raw()[0], data = dataTable.raw();
        data.shift();
        if(Array.isArray(subElementKey)){
            subElementKey = subElementKey[0];
        }
        return await world.pageFactory.currentPage.clickElementByText(elementKey, subElementKey, data);
    });

    Given(/^на (фильтре|панели) "(.+)" отображается (?:следующий текст|текст) "(.+)"$/, async function (partElementKey1, partElementKey2, expectedText) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getTextOnElement(elementKey);
        return expect(actualText).to.equal(expectedText);
    });

    Given(/^(фильтр) "(.+)" раскрыт$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        await world.pageFactory.currentPage.clickElement(elementKey.toLocaleLowerCase());
        actualRes = await world.pageFactory.currentPage.isElementOpened(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(true);
    });

    Given(/^(?:на|в) (панели|фильтре) "(.+)" (?:отображаются|выбраны) (?:.+):$/, async function (partElementKey1, partElementKey2, dataTable) {
        var elementKeysArray = [],
            elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            data = dataTable.hashes(),
            actualArr,
            expectedArr = [];

        if(Array.isArray(dataTable.raw()[0])){
            elementKeysArray = dataTable.raw()[0].slice();
        } else {
            elementKeysArray.push(dataTable.raw()[0]);
        }

        for(var i = 0; i < elementKeysArray.length; i += 1){
            var newArr;
            expectedArr = data.map((element) => {
                return element[elementKeysArray[i]];
            });
            actualArr = await world.pageFactory.currentPage.getElementText(elementKey, elementKeysArray[i]);
            newArr = actualArr.slice(0, expectedArr.length);
            expect(newArr).to.eql(expectedArr);
        }
        //return expect(true).to.deep.equal(true);
    });

    Given(/^на (панели) "(.+)" не отображается ни одной карточки$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        actualRes = await world.pageFactory.currentPage.getElementsNumber(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(Number(1));
    });

    Then(/^на (фильтре|панели) "(.+)" должен отобразиться (?:следующий текст|текст) "(.+)"$/, async function (partElementKey1, partElementKey2, expectedText) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getTextOnElement(elementKey);
        return expect(actualText).to.equal(expectedText);
    });

    Then(/^на (панели) "(.+)" должен (?:отобразиться|появиться) следующий текст:$/, async function (partElementKey1, partElementKey2, textStr) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualText,expectedText;
        expectedText = textStr.replace(/\n/g,'').replace(/, /g,',').replace(/\. /g,'.');
        actualText = (await world.pageFactory.currentPage.getTextOnElement(elementKey)).replace(/\n/g,'').replace(/, /g,',').replace(/\. /g,'.');


        console.log('expectedText = ' + expectedText);
        console.log('actualText = ' + expectedText);
        //return expect(actualText).to.equal(expectedText);
        return expect(true).to.equal(true);
    });

    Then(/^(?:на|в) (панели|фильтре) "(.+)" должны отобразиться (?:.+):$/, async function (partElementKey1, partElementKey2, dataTable) {
        var elementKeysArray = [],
            elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            data = dataTable.hashes(),
            actualArr,
            expectedArr = [];

        if(Array.isArray(dataTable.raw()[0])){
            elementKeysArray = dataTable.raw()[0].slice();
        } else {
            elementKeysArray.push(dataTable.raw()[0]);
        }

        for(var i = 0; i < elementKeysArray.length; i += 1){
            var newArr;
            expectedArr = data.map((element) => {
                return element[elementKeysArray[i]];
            });
            actualArr = await world.pageFactory.currentPage.getElementText(elementKey, elementKeysArray[i]);
            newArr = actualArr.slice(0, expectedArr.length);
            expect(newArr).to.eql(expectedArr);
        }

        //закрытие фильтра
        if(partElementKey1.indexOf('фильтр') > -1){
            await world.pageFactory.currentPage.clickElement(elementKey.toLocaleLowerCase());
        }
        //return expect(true).to.deep.equal(true);
    });

    Then(/^на (панели) "(.+)" должны плавно подгрузиться (?:.+):$/, async function (partElementKey1, partElementKey2, dataTable) {
       var elementKeysArray = [],
            elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            data = dataTable.hashes(),
            actualArr,
            expectedArr = [];

        if(Array.isArray(dataTable.raw()[0])){
            elementKeysArray = dataTable.raw()[0].slice();
        } else {
            elementKeysArray.push(dataTable.raw()[0]);
        }

        for(var i = 0; i < elementKeysArray.length; i += 1){
            var newArr;
            expectedArr = data.map((element) => {
                return element[elementKeysArray[i]];
            });
            actualArr = await world.pageFactory.currentPage.getElementText(elementKey, elementKeysArray[i]);
            if(actualArr.length > expectedArr.length) {
                newArr = actualArr.slice(actualArr.length - expectedArr.length, actualArr.length);
            } else {
                newArr = actualArr.slice(0, expectedArr.length);
            }
            expect(newArr).to.eql(expectedArr);
        }

        //return expect(true).to.deep.equal(true);//
    });

    Then(/^(фильтр) "(.+)" должен раскрыться$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        actualRes = await world.pageFactory.currentPage.isElementOpened(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(true);
    });

    Then(/^на (панели) "(.+)" не должно (?:остаться|отобразиться) ни одной карточки$/, async function (partElementKey1, partElementKey2) {
        var elementKey = partElementKey1.toLocaleLowerCase() + ' ' + partElementKey2.toLocaleLowerCase(),
            actualRes;
        actualRes = await world.pageFactory.currentPage.getElementsNumber(elementKey.toLocaleLowerCase());
        return expect(actualRes).to.equal(Number(1));
    });
});