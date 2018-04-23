'use strict';

class Helper {
    constructor(obj) {
        this.obj = obj;
    };

    //метод возвращает название ключа по заданному тексту
    matchElementKey(obj, elementName) { //elementName - значение, для котрого находится ключ объекта
        var elementRegExp,
            elementKey = 'undefined';
        for (var key in obj) {
            elementRegExp = new RegExp(key, 'i');
            if (elementName.search(elementRegExp) !== -1) {
                elementKey = key;
            }
        }
        return elementKey;
    }

    //метод возвращает второй ключ объекта, если он существует
    getSubElementKey(firstKey, subElementName = firstKey) {//elementName - существующий "первый" ключ
        var subObj = this.obj[firstKey],
            subElementKey = this.matchElementKey(subObj, subElementName);
        return subElementKey;
    }

    //метод возвращает значение объекта по указанного ключам
    //elementName, subElementName - зн-ния, для которых находятся ключи объекта
    getElementLocator(elementName, subElementName = elementName) {
        var firstKey = this.matchElementKey(this.obj, elementName),
            secondKey = this.getSubElementKey(firstKey, subElementName);
        if (secondKey !== 'undefined') {
            return this.obj[firstKey][secondKey];
        } else {
            return this.obj[firstKey];
        }
    }
}

module.exports = Helper;