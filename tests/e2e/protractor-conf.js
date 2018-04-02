
exports.config = {

    specs: ['../../features/**/*.feature'],

    seleniumAddress: 'http://localhost:4444/wd/hub',

    baseUrl: 'http://178.124.206.54/',

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
        require: 'step_definitions/page_steps/*.js',
        keepAlive: false
    }
};
