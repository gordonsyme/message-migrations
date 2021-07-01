(ns message-migrations.core
  (:require [schema.core :as s]))

;; What to demo?
;; 1 - adding a required field
;; 2 - removing a field
;; 3 - renaming a field
;; 4 - changing the type of a field

(def Message
  "A schema for messages that we're going to evolve over time"
  {:name s/Str
   :counter s/Num})

(s/defn ^:always-validate process-message
  [message :- Message]
  {:counter (inc (:counter message))})
