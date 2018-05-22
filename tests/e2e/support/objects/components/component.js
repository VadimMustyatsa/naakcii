'use strict';

class Component{
    constructor(){}

    matchElement(name){
        var key = this.helper.matchElementKey(this.data, name);
        return this.data[key];
    }

}

module.exports = Component;