(ns pantry.subs
    (:require
     [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::foods
 (fn [db]
   (:foods db)))

(re-frame/reg-sub
 ::food
 (fn [db [_ food-name]]
   (get-in db [:foods food-name])))

(re-frame/reg-sub
 ::food-category
 (fn [db [_ food-name]]
   (get-in db [:foods food-name :category])))

(re-frame/reg-sub
 ::food-img-url
 (fn [db [_ food-name]]
   (get-in db [:foods food-name :img])))

(re-frame/reg-sub
 ::target-food
 (fn [db]
   (:target-food db)))
