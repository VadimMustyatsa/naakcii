'use strict';

var world = require('../../support/world');
let cucumber = require('cucumber');

cucumber.defineSupportCode(function({ Given, When, Then, setDefaultTimeout }) {

    setDefaultTimeout(180000);

    Given(/^на панели "(.+)" отображается следующая информация по сети "(.+)":$/, async function (name, text, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            expectedArr = data.reduce((str, element) => {
                return str + element[keys[i]] + ' ';
            },'');
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText(keys[i], 'акционный_товар');
            expect(expectedArr).to.equalIgnoreSpaces(actualArr);
        }
    });

    Given(/^в строке "(.+)" на панели "(.+)" отображается следующая информация:$/, async function (element, name, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            expectedArr = data.reduce((str, element) => {
                return str + element[keys[i]] + ' ';
            },'');
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(keys[i], element.toLocaleLowerCase());
            expect(expectedArr).to.equalIgnoreSpaces(actualArr);
        }
    });

    Given(/^на панели "(.+)" отображается следующая информация по сетям:$/, async function (name, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            expectedArr = data.reduce((str, element) => {
                return str + element[keys[i]] + ' ';
            },'');
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(keys[i]);
            expect(expectedArr).to.equalIgnoreSpaces(actualArr);
        }
    });

    Given(/^я нажимаю (кнопку) "(.+)" (\d) (?:раза|раз) на товаре "(.+)" на панели "(.+)"$/, async function(button, buttonName, qty, text, name) {
        var bttn = button.toLocaleLowerCase() + ' ' + buttonName.toLocaleLowerCase();
        for(var i = 0; i < qty; i += 1){
            await world.pageFactory.currentPage.getElement(name.toLocaleLowerCase(), text).clickElement(bttn, 'акционный_товар');
        }
    });

    Given(/^на панели "(.+)" отображаются следующие неакционные товары:"$/, async function(name, dataTable) {

    });

    Then(/^на панели "(.+)" должна отобразиться следующая информация по сетям:$/, async function (name, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            var newArr;
            expectedArr = data.reduce((str, element) => {
                return str + element[keys[i]] + ' ';
            },'');
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(keys[i]);
            newArr = actualArr.slice(0, expectedArr.length);
            expect(expectedArr).to.equalIgnoreSpaces(actualArr);
        }
    });

    Then(/^на панели "(.+)" должна отобразиться следующая информация по сети "(.+)":$/, async function (name, text, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];
        for(var i = 0; i < keys.length; i += 1){
            expectedArr = data.reduce((str, element) => {
                return str + element[keys[i]] + ' ';
            },'');
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase(), text).getElementText(keys[i], 'акционный_товар');
            expect(expectedArr).to.equalIgnoreSpaces(actualArr);
        }
    });

    Then(/^в строке "(.+)" на панели "(.+)" должна отобразиться следующая информация:$/, async function (element, name, dataTable) {
        var data = dataTable.hashes(),
            actualArr,
            keys = dataTable.raw()[0],
            expectedArr = [];

        for(var i = 0; i < keys.length; i += 1){
            expectedArr = data.reduce((str, element) => {
                return str + element[keys[i]] + ' ';
            },'');
            actualArr = await world.pageFactory.currentPage.getComponent(name.toLocaleLowerCase()).getElementText(keys[i], element.toLocaleLowerCase());
            expect(expectedArr).to.equalIgnoreSpaces(actualArr);
        }
    });

    Then(/^список должен сохраниться$/, async function () {
        await world.pageFactory.currentPage.getComponent('список покупок').closeTabs();
        return await world.pageFactory.getPage('список покупок – naakcii.by');
    });
});