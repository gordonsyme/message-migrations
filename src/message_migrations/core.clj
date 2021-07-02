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
  ;; Now we require the new field name. We could disallow the old name at the
  ;; same time, but we'll accept it a bit longer
  {(s/optional-key :counter) s/Num
   :counter-value s/Num
   :increment-by s/Num})

(s/defn ^:always-validate process-message
  [message :- Message]
  (let [{:keys [increment-by counter-value]} message]
    (-> {:counter counter-value}
        (update :counter + increment-by))))
