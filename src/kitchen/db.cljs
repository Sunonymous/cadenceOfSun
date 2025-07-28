(ns kitchen.db)

(def default-db
  {:food-selection    #{}    ; available foods
   :kitchen-stage     :offer ;; :offer :select :prepare :ready
   :meal-order        #{}
   :kitchen-controls? true  ; "parent mode"
   :kitchen-presets   {}    ; map of user-provided {"name" #{food-selection}}
   :custom-greeting   ""    ;
   :custom-subtitle   ""    ;
   :required-categories #{} ; requires user to order something in each category
   :category-stats    {}    ; map of category -> {:min 1 :max 1}
  })