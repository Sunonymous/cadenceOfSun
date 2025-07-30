(ns pantry.events
    (:require
     [re-frame.core :as    re-frame]
     [cos.events    :refer [->localStorage]]
     [pantry.db     :refer [example-foods]]))

(re-frame/reg-event-db
 ::add-example-foods
 (fn [db _]
   (doseq [{:keys [name category img]}  example-foods]
     (re-frame/dispatch [::add-new-food name category img]))))

(re-frame/reg-event-db
 ::add-new-food
 [->localStorage]
 (fn [db [_ name category img]]
   (let [food {:name name :category category :img img}]
     (assoc-in db [:foods name] food))))

(re-frame/reg-event-db
 ::add-new-blank-food
 [->localStorage]
 (fn [db [_ name]]
   (let [food {:name name :category "" :img ""}]
     ;; jump to the new food on the page
     (js/setTimeout #(.scrollIntoView (.getElementById js/document name)) 100)
     (assoc-in db [:foods name] food)
     )))

(re-frame/reg-event-db
 ::remove-food
 (fn [db [_ food-name]]
   (update db :foods dissoc food-name)))

(re-frame/reg-event-db
 ::target-food
 (fn [db [_ food-name]]
   (assoc db :target-food food-name)))

(re-frame/reg-event-db
 ::clear-target-food
 (fn [db _]
   (assoc db :target-food nil)))

(re-frame/reg-event-db
 ::update-food-name
 [->localStorage]
 (fn [db [_ food-name new-name]]
   (let [food (get-in db [:foods food-name])
         renamed-food (assoc food :name new-name)]
     (re-frame/dispatch [::remove-food food-name])
     (assoc-in db [:foods new-name] renamed-food)
     )))

(re-frame/reg-event-db
 ::update-food-category
 [->localStorage]
 (fn [db [_ food-name new-category]]
   (assoc-in db [:foods food-name :category] new-category)))

(re-frame/reg-event-db
 ::update-food-image
 [->localStorage]
 (fn [db [_ food-name new-url]]
   (assoc-in db [:foods food-name :img] new-url)))