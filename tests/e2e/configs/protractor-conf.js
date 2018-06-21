var moment = require("moment"),
    fse = require("fs-extra"),
    reporter = require("cucumber-html-reporter"),
    reportDir = "reports/report_" + moment().format("YYYYMMDD_HHmmss");

exports.config = {

    specs: ['../../../features/**/*.feature'],

    seleniumAddress: 'http://localhost:4444/wd/hub',
    capabilities: {
        browserName: 'chrome',
        'chromeOptions': {
            args: ['--window-size=1680,1050',
                    '--no-sandbox']
        }
    },
    ignoreUncaughtExceptions: true,

    getPageTimeout: 70000,
    allScriptsTimeout: 120000,

    framework: 'custom',
    frameworkPath: require.resolve('protractor-cucumber-framework'),

    cucumberOpts: {
        require: ['../support/world.js',
            '../step_definitions/page_steps/*.js',
            '../step_definitions/*.js'],
        format: ['json:' + reportDir + '/cucumber-report.json'],
        keepAlive: false
    },
    onPrepare : function() {
        var chai = require('chai');
        chaiAsPromised = require('chai-as-promised');
        chaiString = require('chai-string');
        expect = chai.expect;
        chai.use(chaiAsPromised);
        chai.use(chaiString);
        fse.mkdirsSync(reportDir);
    },

    onComplete: function () {
        var options = {
            theme: "bootstrap",
            jsonFile: reportDir + '/cucumber-report.json',
            output: reportDir + '/cucumber-report.html',
            ignoreBadJsonFile: true,
            reportSuiteAsScenarios: true,
            launchReport: true
        };
        reporter.generate(options);
    }
};
