var cucumber = require('cucumber');

cucumber.defineSupportCode(function({After, setDefaultTimeout}) {
    setDefaultTimeout(180000);
    After(function (scenario) {
        var world = this;
        if(scenario.result.status === 'failed') {
            return browser.takeScreenshot().then(function (buffer) {
                return world.attach(buffer, 'image/png');
            });
        }
    });
});