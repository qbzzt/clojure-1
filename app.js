/*eslint-env node*/

//------------------------------------------------------------------------------
// node.js starter application for Bluemix
//------------------------------------------------------------------------------

// This application uses express as its web server
// for more info, see: http://expressjs.com
var express = require('express');

// cfenv provides access to your Cloud Foundry environment
// for more info, see: https://www.npmjs.com/package/cfenv
var cfenv = require('cfenv');


// Get a Clojure environment
var cljs = require('clojurescript-nodejs');
	
cljs.eval('(ns c (:require clojure.string)) (defn bold [str] (clojure.string/join ["<b>" str "</b>"]))');	
	
// console.log("Function definition: " + 
//	JSON.stringify(cljs.eval("(ns test (:require [clojure.string :as str])) (defn add-fun [a b] (str/join [a '+' b '=' (+ a b)]))")));	
	
	
// create a new express server
var app = express();


app.get("/test", function(req, res) {
	res.send("The result is: " + cljs.eval('(ns c) (bold "xx")'));
});

// serve the files out of ./public as our main files
app.use(express.static(__dirname + '/public'));

// get the app environment from Cloud Foundry
var appEnv = cfenv.getAppEnv();

// start server on the specified port and binding host
app.listen(appEnv.port, '0.0.0.0', function() {
  // print a message when the server starts listening
  console.log("server starting on " + appEnv.url);
});
