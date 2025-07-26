(ns keer.subs
  (:require
   [re-frame.core :as re-frame]
   [keer.data :refer [tones-with-flats tones-with-sharps]]
  ))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::starting-index
 (fn [db]
   (:starting-index db)))

(re-frame/reg-sub
 ::tone-sequence
 (fn [db]
   (:tone-sequence db)))

(re-frame/reg-sub
 ::use-sharps?
 (fn [db]
   (:use-sharps? db)))

(re-frame/reg-sub
 ::semitone-adjustment
 (fn [db] (:semitone-adjustment db)))

(re-frame/reg-sub
 ::previous-tone
 (fn [db] (:previous-tone db)))

(re-frame/reg-sub
 ::number-of-laps
 (fn [db] (:laps db)))