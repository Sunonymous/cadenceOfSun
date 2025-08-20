(ns kitchen.db)

(def default-db
  {:food-selection    #{}    ; available foods
   :kitchen-stage     :offer ; :offer :greet :order :confirm :prepare :ready
   :meal-order        #{}    ; foods chosen by diner
   :kitchen-controls? true   ; "parent mode"
   :kitchen-presets   {}     ; map of user-provided {"name" #{food-selection}}
   :custom-greeting   ""     ;
   :custom-subtitle   ""     ;
   :required-categories #{}  ; requires user to order something in each category
   :category-stats    {}     ; map of category -> {:min 1 :max 1}
   :max-total-foods   nil    ; cannot choose more foods than this (no max if nil)
  })