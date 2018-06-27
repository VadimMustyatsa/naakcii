'use strict';

var world = require('../../support/world'),
    ShoppingListPage = require('../../support/objects/pages/shoppingListPage');
let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout }) {

    setDefaultTimeout(180000);

    Given(/^я (?:нажимаю на|выбираю) (пункт|категорию|товар) "(.+)" (?:на|в) (панели|фильтре) "(.+)"$/, async function(subElement, text, element, name) {
        await browser.executeScript('window.scrollTo(0,0);');
        return await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).clickElement(subElement);
    });

    Given(/^я выбираю следующие (?:торговые сети в|подкатегории на) (фильтре|панели) "(.+)":$/, async function(element, name, dataTable) {
        var key = dataTable.raw()[0],
            data = dataTable.hashes().map((elements) => { return elements[key]; });
        for(var i = 0; i < data.length; i += 1){
            await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), data[i]).clickElement(key[0]);
        }
    });

    Given(/^на (фильтре|панели) "(.+)" отображается (?:следующий текст|текст) "(.+)"$/, async function (element, name, expectedText) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(component);
        return expect(actualText).to.equalIgnoreSpaces(expectedText);
    });

    Given(/^(фильтр) "(.+)" раскрыт$/, async function (element, name) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualResult;
        await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).clickElement(component);
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).isElementOpened();
        return expect(actualResult).to.equal(true);
    });

    Given(/^(?:на|в) (панели|фильтре) "(.+)" (?:отображаются|выбраны) (?:.+):$/, async function (element, name, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            elementKeysArray,
            expectedArr = [];

        if(world.pageFactory.currentPage instanceof ShoppingListPage){
            expect(true).to.eql(true);
        } else {
            if (Array.isArray(dataTable.raw()[0])) {
                elementKeysArray = dataTable.raw()[0].slice();
            } else {
                elementKeysArray.push(dataTable.raw()[0]);
            }

            for (var i = 0; i < elementKeysArray.length; i += 1) {
                var newArr;
                expectedArr = data.map((element) => {
                    return element[elementKeysArray[i]].toLocaleLowerCase();
                });
                if (elementKeysArray[i].indexOf('статус') === -1) {
                    actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(elementKeysArray[i]);
                } else {
                    actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getStatus(elementKeysArray[i]);
                }
                newArr = (actualArr.slice(0, expectedArr.length)).map((element) =>{
                    return element.toLocaleLowerCase();
                });
                expect(newArr).to.eql(expectedArr);
            }
        }
    });

    Given(/^на (панели) "(.+)" отображается следующий текст:$/, async function (element, name, expectedText) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(component);
        return expect(actualText).to.equalIgnoreSpaces(expectedText);
    });

    Given(/^на (панели) "(.+)" не отображается ни одной карточки$/, async function (element, name) {
        var actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementsNumber(element);
        return expect(actualResult).to.equal(0);
    });

    Given(/^на панели "(.+)" отображается (товар) "(.+)"$/, async function (name, element, text) {
        var actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).isElementDisplayed(element);
        return expect(actualResult).to.equal(true);
    });

    Given(/^(товар) "(.+)" раскрыт на панели "(.+)"$/, async function (element, text, name) {
        var actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).isElementOpened();
        return expect(actualResult).to.equal(true);
    });

    Given(/^на панели "(.+)" отображается следующая (информация) для товара "(.+)":$/, async function (name, element, text, dataTable) {
        var data = dataTable.raw(),
            actualResult,
            expectedResult;
        data.shift();
        expectedResult = data.reduce((str, elem) => {
            return str + elem + ' ';
        }, '');
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText(element) + ' ';
        actualResult += await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText('поле');
        return expect(actualResult).to.equalIgnoreSpaces(expectedResult);
    });

    Then(/^на (фильтре|панели) "(.+)" должен отобразиться (?:следующий текст|текст) "(.+)"$/, async function (element, name, expectedText) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(component);
        return expect(actualText).to.equalIgnoreSpaces(expectedText);
    });

    Then(/^на (панели) "(.+)" должен (?:отобразиться|появиться) следующий текст:$/, async function (element, name, expectedText) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            actualText;
        actualText = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(component);
        return expect(actualText).to.equalIgnoreSpaces(expectedText);
    });

    Then(/^(?:на|в) (панели|фильтре) "(.+)" должны отобразиться (?:.+):$/, async function (element, name, dataTable) {
        var component = element.toLocaleLowerCase() + ' ' + name.toLocaleLowerCase(),
            data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            var newArr;
            expectedArr = data.map((element) => {
                return element[keys[i]].toLocaleLowerCase();
            });
            if(keys[i].indexOf('статус') === -1) {
                actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(keys[i]);
            } else {
                actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getStatus(keys[i]);
            }
            newArr = (actualArr.slice(0, expectedArr.length)).map((element) =>{
                return element.toLocaleLowerCase();
            });
            expect(newArr).to.eql(expectedArr);
        }
        //закрытие фильтра
        if(element.indexOf('фильтр') > -1){
            await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).clickElement(component);
        }
    });

    Then(/^фильтр "(.+)" должен раскрыться$/, async function (name) {
        var actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).isElementOpened();
        return expect(actualResult).to.equal(true);
    });

    Then(/^товар "(.+)" должен раскрыться на панели "(.+)"$/, async function (text, name) {
        var actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).isElementOpened();
        return expect(actualResult).to.equal(true);
    });

    Then(/^на панели "(.+)" должна отобразиться следующая (информация) для товара "(.+.)":$/, async function (name, element, text, dataTable) {
        var data = dataTable.raw(),
            actualResult,
            expectedResult;
        data.shift();
        expectedResult = data.reduce((str, elem) => {
            return str + elem + ' ';
        }, '');
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText(element);
        actualResult += await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText('поле');
        return expect(actualResult).to.equalIgnoreSpaces(expectedResult);
    });

    Then(/^на (панели) "(.+)" должны плавно подгрузиться (?:.+):$/, async function (element, name, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            var newArr;
            expectedArr = data.map((element) => {
                return element[keys[i]];
            });
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(keys[i]);
            newArr = actualArr.slice((actualArr.length - expectedArr.length - 1), expectedArr.length);
            expect(newArr).to.eql(expectedArr);
        }
    });

    Then(/^на (панели) "(.+)" не должно (?:остаться|отобразиться) ни (?:одной|одного) (карточки|товара)$/, async function (name, element) {
        var actualResult;
        actualResult = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementsNumber(element);
        return expect(actualResult).to.equal(0);
    });

    Then(/^на карточке "(.+)" на панели "(.+)" должно отобразиться (количество) товара "(\d)"$/, async function (text, name, element, expectedQty) {
        var actualQty;
        actualQty = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText(element);
        return expect(actualQty[0]).to.equalIgnoreSpaces(expectedQty);
    });
});