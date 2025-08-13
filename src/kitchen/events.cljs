(ns kitchen.events
  (:require
   [re-frame.core :as re-frame]
   [tools.util    :refer [inc-wrap-index dec-wrap-index]]
   [kitchen.data  :refer [foods]]
   [cos.events    :refer [->localStorage]]))

(re-frame/reg-event-db
 ::toggle-food-selection
 (fn [db [_ food]]
   (if ((:food-selection db) food)
     (update db :food-selection disj food)
     (update db :food-selection conj food))))

(re-frame/reg-event-db
 ::toggle-order-selection
 (fn [db [_ food]]
   (if ((:meal-order db) food)
     (update db :meal-order disj food)
     (update db :meal-order conj food))))

(re-frame/reg-event-db
 ::clear-order
 (fn [db _]
   (assoc db :meal-order #{})))

(re-frame/reg-event-db
 ::offer-all-foods
 (fn [db _] ;; TODO this still references hard-coded data!
   (assoc db :food-selection (set (keys foods)))))

(re-frame/reg-event-db
 ::offer-food
 (fn [db [_ food]]
   (update db :food-selection conj food)))

(re-frame/reg-event-db
 ::remove-offered-food
 (fn [db [_ food]]
   (update db :food-selection disj food)))

(re-frame/reg-event-db
 ::remove-all-offered-foods
 (fn [db _]
   (assoc db :food-selection #{})))

(def stages [:offer :greet :order :confirm :prepare :ready])

(defn index-of [v item]
  (some (fn [[i x]] (when (= x item) i)) (map-indexed vector v)))

(re-frame/reg-event-db
 ::set-kitchen-stage
 (fn [db [_ stage]]
   (assoc db :kitchen-stage stage)))

(re-frame/reg-event-db
 ::next-stage
 [->localStorage]
 (fn [db _]
   (let [current-index (index-of stages (:kitchen-stage db))]
     (assoc db :kitchen-stage (nth stages (inc-wrap-index stages current-index))))))

(re-frame/reg-event-db
 ::previous-stage
 [->localStorage]
 (fn [db _]
   (let [current-index (index-of stages (:kitchen-stage db))]
     (assoc db :kitchen-stage (nth stages (dec-wrap-index stages current-index))))))

(re-frame/reg-event-db
 ::toggle-kitchen-controls
 (fn [db _]
   (update db :kitchen-controls? not)))

(re-frame/reg-event-db
 ::save-offer-preset
 [->localStorage]
 (fn [db [_ name food-selection]]
   (assoc-in db [:kitchen-presets name] food-selection)))

(re-frame/reg-event-db
 ::load-offer-preset
 (fn [db [_ name]]
   (assoc db :food-selection (get-in db [:kitchen-presets name]))))

(re-frame/reg-event-db
 ::delete-offer-preset
 [->localStorage]
 (fn [db [_ name]]
   (update db :kitchen-presets dissoc name)))

(re-frame/reg-event-db
 ::set-custom-greeting
 (fn [db [_ greeting]]
   (assoc db :custom-greeting greeting)))

(re-frame/reg-event-db
 ::set-custom-subtitle
 (fn [db [_ subtitle]]
   (assoc db :custom-subtitle subtitle)))

(re-frame/reg-event-db
 ::require-category-in-order
 [->localStorage]
 (fn [db [_ category]]
   (update db :required-categories conj category)))

(re-frame/reg-event-db
 ::unrequire-category-in-order
 [->localStorage]
 (fn [db [_ category]]
   (update db :required-categories disj category)))

(re-frame/reg-event-db
 ::set-category-min
 [->localStorage]
 (fn [db [_ category min]]
   (assoc-in db [:category-stats category :min] min)))

(re-frame/reg-event-db
 ::set-category-max
 [->localStorage]
 (fn [db [_ category  max]]
   (assoc-in db [:category-stats category :max] max)))

(re-frame/reg-event-db
 ::reset-kitchen
 (fn [_ _]
   (re-frame/dispatch [::remove-all-offered-foods])
   (re-frame/dispatch [::clear-order])))