(ns board.events (:require
  [re-frame.core :as re-frame]
  [cos.events    :refer [->localStorage]]
))

(re-frame/reg-event-db
 ::add-line
 [->localStorage]
 (fn [db [_ text]]
   (update db :lines conj text)))


;; TODO board delete line adds to recovery drawer
(re-frame/reg-event-db
 ::delete-line
 (fn [db [_ text]]
   (update db :lines (fn [lines] (filter #(not= text %) lines)))))