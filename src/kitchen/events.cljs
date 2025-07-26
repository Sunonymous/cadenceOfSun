(ns kitchen.events
  (:require
   [re-frame.core :as re-frame]
   [tools.util    :refer [inc-wrap-index dec-wrap-index]]
   [kitchen.data  :refer [foods]]))

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
 (fn [db _]
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
 ::next-stage
 (fn [db _]
   (let [current-index (index-of stages (:kitchen-stage db))]
     (assoc db :kitchen-stage (nth stages (inc-wrap-index stages current-index))))))

(re-frame/reg-event-db
 ::previous-stage
 (fn [db _]
   (let [current-index (index-of stages (:kitchen-stage db))]
     (assoc db :kitchen-stage (nth stages (dec-wrap-index stages current-index))))))

(re-frame/reg-event-db
 ::toggle-kitchen-controls
 (fn [db _]
   (update db :kitchen-controls? not)))

(re-frame/reg-event-db
 ::save-offer-preset
 (fn [db [_ name food-selection]]
   (assoc-in db [:kitchen-presets name] food-selection)))

(re-frame/reg-event-db
 ::load-offer-preset
 (fn [db [_ name]]
   (assoc db :food-selection (get-in db [:kitchen-presets name]))))

(re-frame/reg-event-db
 ::delete-offer-preset
 (fn [db [_ name]]
   (update db :kitchen-presets dissoc name)))

;; TODO add db persistance to these events!