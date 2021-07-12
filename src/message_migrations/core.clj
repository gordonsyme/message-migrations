(ns message-migrations.core
  (:require [schema.core :as s]))

;; What to demo?
;; 1 - adding a required field ✅
;; 2 - removing a field ✅
;; 3 - renaming a field ✅
;; 4 - changing the type of a field ✅
;; 5 - using versions to make a breaking change ✅

(def V1Message
  "A schema for messages that we're going to evolve over time"
  {:counter-value s/Num
   :increment-by [s/Num]})

(def V2Message
  {:operator (s/enum "inc" "dec" "mult")
   :operand s/Num
   :counter s/Num})

(def Envelope
  "This schema is for the message envelope. There's a numeric version field
  which must be from the set of supported values, and a message field which
  this schema allows to be anything. Each message version has its own schema to
  enforce the shape of the message."
  {:version (s/enum 1 2)
   :message s/Any})

(defn ^:private apply-counter-operation
  "Different versions of messages should still ideally trigger the same thing,
  so we hoist the core idea of message processing into this version-independent
  function.

  If the functions to process different message versions are completely
  different then that might be a sign that you really need a different message
  altogether."
  [op counter-value x]
  (case op
    "inc" (+ counter-value x)
    "dec" (- counter-value x)
    "mult" (* counter-value x)))

(s/defn ^:always-validate process-v1-message
  [message :- V1Message]
  (let [{:keys [increment-by counter-value]} message
        increment-amount (reduce + 0 increment-by)]
    {:counter (apply-counter-operation "inc"
                                       counter-value
                                       increment-amount)}))

(s/defn ^:always-validate process-v2-message
  [message :- V2Message]
  (let [{:keys [operator operand counter]} message]
    {:counter (apply-counter-operation operator
                                       counter
                                       operand)}))

(s/defn ^:always-validate deal-with-envelope
  [envelope :- Envelope]
  (let [version (:version envelope)]
    (case version
      1 (process-v1-message (:message envelope))
      2 (process-v2-message (:message envelope)))))
