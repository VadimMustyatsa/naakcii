## Running end-to-end tests

### Open a command window and run:

    npm install 

### Run webdriver server:

    npm run wd-server
	
### Run tests:

    npm run protractor
	
**Note**: Change the following code to generate reports correctly. Go to  */node_modules/cucumber-html-reporter/lib/reporter.js*:

	var *sanitize* = function (name, find) {
		var unsafeCharacters = find || /[/\|:"*?<>]/g;
		**if (name !== undefined) {**
			name = name.trim().replace(unsafeCharacters, '_');
		**}**
		return name;
	};