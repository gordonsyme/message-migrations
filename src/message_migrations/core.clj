(ns message-migrations.core
  (:require [schema.core :as s]))

;; What to demo?
;; 1 - adding a required field ✅
;; 2 - removing a field ✅
;; 3 - renaming a field ✅
;; 4 - changing the type of a field ✅
;; 5 - using versions to make a breaking change

(def V1Message
  "A schema for messages that we're going to evolve over time"
  {:counter-value s/Num
   :increment-by [s/Num]})

(def Envelope
  "This schema is for the message envelope. There's a numeric version field
  which must be from the set of supported values, and a message field which
  this schema allows to be anything. Each message version has its own schema to
  enforce the shape of the message."
  {:version (s/eq 1)
   :message s/Any})

(s/defn ^:always-validate process-v1-message
  [message :- V1Message]
  (let [{:keys [increment-by counter-value]} message
        increment-amount (reduce + 0 increment-by)]
    (-> {:counter counter-value}
        (update :counter + increment-amount))))

(s/defn ^:always-validate deal-with-envelope
  [envelope :- Envelope]
  (let [version (:version envelope)]
    (case version
      1 (process-v1-message (:message envelope)))))
