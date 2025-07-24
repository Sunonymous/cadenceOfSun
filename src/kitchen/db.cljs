(ns kitchen.db)

(def default-db
  {:food-selection    #{} ; available foods
   :kitchen-stage     :offer ;; :offer :select :prepare :ready
   :meal-order        #{}
   :kitchen-controls? true ; "parent mode"
  })