(ns kitchen.subs
  (:require
   [re-frame.core :as re-frame]))

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