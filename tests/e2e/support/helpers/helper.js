'use strict';

class Helper {
    constructor() {
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
    getSubElementKey(obj, firstKey, subElementName = firstKey) {//elementName - существующий "первый" ключ
        var subObj = obj[firstKey],
            subElementKey = this.matchElementKey(subObj, subElementName);
        return subElementKey;
    }

    //метод возвращает значение объекта по указанного ключам
    //elementName, subElementName - зн-ния, для которых находятся ключи объекта
    getElementLocator(obj, elementName, subElementName = elementName) {
        var firstKey = this.matchElementKey(obj, elementName),
            secondKey = this.getSubElementKey(obj, firstKey, subElementName);
        if (secondKey !== 'undefined') {
            return obj[firstKey][secondKey];
        } else {
            return obj[firstKey];
        }
    }
}

module.exports = Helper;