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