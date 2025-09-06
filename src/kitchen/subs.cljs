(ns kitchen.subs
  (:require
   [re-frame.core :as re-frame]
  ))

(re-frame/reg-sub
 ::selected-foods
 (fn [db]
   (:food-selection db)))

(re-frame/reg-sub
 ::kitchen-stage
 (fn [db]
   (:kitchen-stage db)))

(re-frame/reg-sub
 ::meal-order
 (fn [db]
   (:meal-order db)))

(re-frame/reg-sub
 ::show-kitchen-controls?
 (fn [db]
   (:kitchen-controls? db)))

(re-frame/reg-sub
 ::food-categories
 (fn [db]
   (set (map :category (vals (:foods db))))))

(re-frame/reg-sub
 ::kitchen-presets
 (fn [db]
   (:kitchen-presets db)))

(re-frame/reg-sub
 ::custom-greeting
 (fn [db]
   (:custom-greeting db)))

(re-frame/reg-sub
 ::custom-subtitle
 (fn [db]
   (:custom-subtitle db)))

(re-frame/reg-sub
 ::offered-categories
 (fn [db]
   (set (map :category (map #((:foods db) %) (:food-selection db))))))

(re-frame/reg-sub
 ::required-categories
 (fn [db]
   (:required-categories db)))

 (re-frame/reg-sub
  ::num-of-order-items-of-category
  (fn [db [_ category]]
    (count (filter #(= category (:category ((:foods db) %))) (:meal-order db)))))

(re-frame/reg-sub
 ::category-min
 (fn [db [_ category]]
   (or (get-in db [:category-stats category :min]) 1)))

(re-frame/reg-sub
 ::category-max
 (fn [db [_ category]]
   (or (get-in db [:category-stats category :max]) 1)))

(re-frame/reg-sub
 ::max-total-foods
 (fn [db]
   (:max-total-foods db)))

(re-frame/reg-sub
 ::order-less-than-max-total-foods?
 :<- [::meal-order]
 :<- [::max-total-foods]
 (fn [[meal-order max-total-foods] _]
   (if (nil? max-total-foods)
     true
     (< (count meal-order) max-total-foods))))

(re-frame/reg-sub
 ::food-name ; show nickname for display or fallback to food name
 (fn [db [_ food-name]]
   (let [nickname (get-in db [:foods food-name :nickname])]
     (or nickname food-name))))