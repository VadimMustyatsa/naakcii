'use strict'

var PageFactory = require('./objects/pageFactory');

class World{
    constructor() {
        this.pageFactory = new PageFactory();
    }
}

module.exports = new World;