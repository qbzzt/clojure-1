(ns my-ns (:require clojure.string 
				[cljs.nodejs :as node]))

; Get the application environment
(def appEnv (.getAppEnv (js/require "cfenv")))

; Create an express application
(def express (js/require "express"))
(def app (js* "require('express')()"))

; Static files
(.use app ((aget express "static") "public"))

; Respond to a request
(.get app "/trivial" (fn [req res] (.send res "Hello")))


; Start listening
(.listen app 
	(aget appEnv "port") 
	"0.0.0.0"
	(fn [] 
		(.log js/console (aget appEnv "url"))
	)
)


;;;;; End of the trivial example, start of the input processing example


; Go back to the form
(def goBackForm "<hr /><a href=\"/form.html\">Go back</a>");

; Process a part of the path
(.get app "/process-path/:param"
	(fn [req res]
		(.send res
			(clojure.string/join [
				(js* "req.params.param")
				goBackForm
			])
		)
	)
)


; Respond to GET requests (read the query)
(.get app "/process-form" 
	(fn [req res]
		(.send res
			(clojure.string/join [
				(js* "req.query.get")
				goBackForm
			])
		)
	)
)

; Parse the body
(.post app "*" 
	(js* "require('body-parser').urlencoded({extended: true})")
)

; Respond to POST requests
(.post app "/process-form" 
	(fn [req res]
		(.send res
			(clojure.string/join [
				(js* "req.body.post")
				goBackForm
			])
		)
	)
)


; Given a request object, return a map with all the parameters
(defn getParams [req] 
	(let [    ; Define local variables
		queryMap (js->clj (js* "req.query"))
		bodyMap (js->clj (js* "req.body"))
		paramMap (js->clj (js* "req.params"))		
	]
	(merge queryMap bodyMap paramMap)
	)   ; end of let
) ; end of defn


; Respond requests with parameters from all over
(.all app "/merge/:pathparam" 
	(fn [req res]
		(let [ ; local variables
				params (getParams req)
			]
			(.send res
				(clojure.string/join [
					(.stringify js/JSON (clj->js params))
					goBackForm
					])  ; end of clojure.string/join
			)  ; end of res.send
		)	; end of let
	) ; end of fn [req res]
)   ; end of app.all