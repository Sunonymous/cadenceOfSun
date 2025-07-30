(ns cos.events
  (:require
   [cos.db        :as db]
   [keer.db       :as keer-db]
   [re-frame.core :as re-frame]
   [pantry.db     :as pantry-db]
   [kitchen.db    :as kitchen-db]
   [cljs.reader :refer [read-string]]
   ))

(def localStorage-key "cos-db")

(defn db->local-storage
  [db]
  (js/localStorage.setItem localStorage-key (pr-str (dissoc db :current-route))))

(re-frame/reg-cofx
 :localStorage
 (fn [coeffects storage-key]
   (assoc coeffects :localStorage
          (js/localStorage.getItem storage-key))))

(def ->localStorage (re-frame/after db->local-storage))

(re-frame/reg-event-fx
 ::initialize-db
 [(re-frame/inject-cofx :localStorage localStorage-key)]
 (fn [cofx _]
   (let [persisted-db (read-string (:localStorage cofx))]
     ;; TODO would be better to use a spec...
     (assoc cofx :db (if persisted-db
                        (merge db/default-db persisted-db)
                        (merge keer-db/default-db    ;; Add DB for Keer Tool
                               pantry.db/default-db  ;; Add DB for Pantry Tool
                               kitchen-db/default-db ;; Add DB for Kitchen Tool
                               db/default-db))))))   ;; Home DB
