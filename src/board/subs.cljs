(ns board.subs
  (:require
   [re-frame.core :as re-frame]
))

(re-frame/reg-sub
 ::lines
 (fn [db] (:lines db)))

(re-frame/reg-sub
 ::last-used
 (fn [db] (:last-used db)))

(re-frame/reg-sub
 ::sections
 (fn [db] (:sections db)))

(re-frame/reg-sub
 ::active-section
 (fn [db] (:active-section db)))

(re-frame/reg-sub
 ::lines-in-section
 (fn [db [_ section-title]]
   (get-in db [:sections section-title])))

(re-frame/reg-sub
 ::focused-line
 (fn [db] (:focused-line db)))
