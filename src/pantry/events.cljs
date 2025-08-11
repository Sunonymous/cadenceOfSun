(ns pantry.events
    (:require
     [re-frame.core :as    re-frame]
     [cos.events    :refer [->localStorage]]
     [pantry.db     :refer [example-foods]]
     [cljs.reader   :refer [read-string]]))

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
 ::update-food-nickname
 [->localStorage]
 (fn [db [_ food-name new-nickname]]
   (assoc-in db [:foods food-name :nickname] new-nickname)))

(re-frame/reg-event-db
 ::update-food-image
 [->localStorage]
 (fn [db [_ food-name new-url]]
   (assoc-in db [:foods food-name :img] new-url)))

;; Data Export
(re-frame/reg-fx
 ::export-data
 (fn [data]
   (let [data-str (str "data:text/edn;charset=utf-8,"
                       (js/encodeURIComponent (pr-str data)))
         anchorElem (js/document.getElementById "downloadDataAnchor")]
     (set! (.-href anchorElem) data-str)
     (set! (.-download anchorElem) "pantry_foods.edn")
     (.click anchorElem)
     (js/alert "Data has been downloaded to 'pantry_foods.edn'"))))

(re-frame/reg-event-fx
 ::export-pantry-foods
 (fn [cofx _]
   {::export-data (get-in cofx [:db :foods])}))

(re-frame/reg-event-db
 ::import-pantry-foods
 [->localStorage]
 (fn [db [_ edn-data]]
   (let [data (read-string edn-data)]
     (assoc db :foods data))))

(re-frame/reg-event-db
 ::clear-pantry
 (fn [db _]
   (assoc db :foods {})))

(re-frame/reg-event-db
 ::load-foods-from-string
 (fn [db [_ str-data]]
   (let [next-foods (read-string str-data)]
     (if (and next-foods
              ; (s/valid? ::minddrop.db/pool next-pool) ; TODO need spec!
              )
       (assoc db :foods next-foods)
       (do ;; this branch should never trigger; just held here for eventual improvement
         (js/alert "Invalid data.")
         db)))))
