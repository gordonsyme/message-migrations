(ns message-migrations.core
  (:require [schema.core :as s]))

;; What to demo?
;; 1 - adding a required field ✅
;; 2 - removing a field ✅
;; 3 - renaming a field
;; 4 - changing the type of a field

(def Message
  "A schema for messages that we're going to evolve over time"
  ;; Renaming a field combines the techniques to add a field and remove a field
  ;; First let's make the old field name optional, and add a new optional field
  ;; for the new name.
  ;;
  ;; We also need to decide what the application behaviour should be. In this
  ;; case we will:
  ;; * Require at least one of the keys
  ;; * If both keys are passed, accept the new one
  ;; * The output message format will still use `:counter`
  {(s/optional-key :counter) s/Num
   (s/optional-key :counter-value) s/Num
   :increment-by s/Num})

(s/defn ^:always-validate process-message
  [message :- Message]
  (let [{:keys [increment-by counter counter-value]} message]
    (when (and (nil? counter)
               (nil? counter-value))
      (throw (ex-info "Must pass one of \"counter\" or \"counter-value\"" {})))
    (-> {:counter (or counter-value
                      counter)}
        (update :counter + increment-by))))
