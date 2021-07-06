(ns message-migrations.server
  (:require [cheshire.core :as json]
            [ring.adapter.jetty :as jetty]
            [message-migrations.core :refer (deal-with-envelope)])
  (:import clojure.lang.ExceptionInfo))

(defn handler
  [request]
  (cond
    (not= "/message" (:uri request))
    {:status 404}

    (not= :post (:request-method request))
    {:status 400}

    :else
    (try
      (let [response (-> (:body request)
                         slurp
                         (json/parse-string true)
                         deal-with-envelope)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string response)})
      (catch ExceptionInfo e
        {:status 400
         :headers {"Content-Type" "text/plain"}
         :body (ex-message e)})
      (catch Exception _
        {:status 500}))))

(defn run-in-repl
  []
  (jetty/run-jetty handler {:port 3031
                            :join? false}))

(defn -main
  [& _args]
  (jetty/run-jetty handler {:port 3031}))
