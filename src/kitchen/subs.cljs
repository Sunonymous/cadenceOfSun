(ns kitchen.subs
  (:require
   [re-frame.core :as re-frame]
   [kitchen.data :refer [foods]]))

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
 (fn [_db] ;; TODO once pantry gets implemented into db, get categories from there
   (set (map :category (vals foods)))))

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