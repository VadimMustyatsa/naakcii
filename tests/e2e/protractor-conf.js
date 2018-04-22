
exports.config = {

    specs: ['../../features/**/*.feature'],

    seleniumAddress: 'http://localhost:4444/wd/hub',
    capabilities: {
        browserName: 'chrome',
        'chromeOptions': {
            args: ['--no-sandbox']
        }
    },
    ignoreUncaughtExceptions: true,

    getPageTimeout: 70000,
    allScriptsTimeout: 120000,

    framework: 'custom',
    frameworkPath: require.resolve('protractor-cucumber-framework'),

    cucumberOpts: {
        require: ['support/world.js',
            'step_definitions/page_steps/*.js',
            'step_definitions/*.js'],
        keepAlive: false
    },
    onPrepare : function() {
        var chai = require('chai');
        chaiAsPromised = require('chai-as-promised');
        expect = chai.expect;
        chai.use(chaiAsPromised);
    }
};
