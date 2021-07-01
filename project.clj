(defproject message-migrations "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-http "3.12.0"]
                 [cheshire "5.10.0"]
                 [prismatic/schema "1.1.12"]
                 [ring/ring-core "1.9.3"]
                 [ring/ring-jetty-adapter "1.9.3"]]
  :repl-options {:init-ns message-migrations.core})
