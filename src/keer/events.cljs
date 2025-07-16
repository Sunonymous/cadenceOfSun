(ns keer.events
  (:require
   [keer.db       :as db]
   [re-frame.core :as re-frame]
   [keer.util     :refer [build-sequence]]
   )
)

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-starting-index
 (fn [db [_ next-index]]
   (assoc db :starting-index next-index)))

(re-frame/reg-event-db
 ::toggle-sharps
 (fn [db _]
   (update db :use-sharps? #(not %))))

(re-frame/reg-event-db
 ::set-tone-sequence
 (fn [db [_ tone-sequence]]
   (update db :tone-sequence #(conj % 5))))

;; This reset should be used to reset the tone sequence to its default state
(re-frame/reg-event-db
 ::reset-tone-sequence
 (fn [db _]
            (-> db
                (assoc :previous-tone nil)
                (assoc :tone-sequence (build-sequence (:starting-index db) (:semitone-adjustment db))))))

;; This one is for lap mode, where the sequence needs to appear endless
(re-frame/reg-event-db
 ::complete-tone-sequence
 (fn [db _]
            (-> db
                (assoc :previous-tone (first (:tone-sequence db)))
                (assoc :tone-sequence (build-sequence (:starting-index db) (:semitone-adjustment db)))

                (assoc :laps (if (= (:starting-index db) (:previous-lap db))
                               (inc (:laps db))
                               1))
                (assoc :previous-lap (:starting-index db)))))

(re-frame/reg-event-db
 ::cycle-tone-sequence
 (fn [db _]
            (-> db
                (assoc :previous-tone (first (:tone-sequence db)))
                (update :tone-sequence rest))))

(re-frame/reg-event-db
 ::set-semitone-adjustment
 (fn [db [_ next-semitone-adjustment]]
   (assoc db :semitone-adjustment next-semitone-adjustment)))