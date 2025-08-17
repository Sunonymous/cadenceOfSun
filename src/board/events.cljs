(ns board.events (:require
  [re-frame.core :as re-frame]
  [cos.events    :refer [->localStorage]]
))

(re-frame/reg-event-db
 ::add-new-string
 [->localStorage]
 (fn [db [_ text]]
   (update db :lines conj text)))

(re-frame/reg-event-db
 ::delete-string
 (fn [db [_ text]]
   (update db :lines (fn [lines] (filter #(not= text %) lines)))))