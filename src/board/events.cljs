(ns board.events (:require
  [re-frame.core :as re-frame]
  [cos.events    :refer [->localStorage]]
))

;; IMPORTANT NOTE
; there is a function (::events/check-day) that needs to be placed in core that runs every app startup.
; if the board is opened on a different day, it is wiped clean before display

(re-frame/reg-event-db
 ::add-line
 [->localStorage]
 (fn [db [_ text]]
   (update db :lines conj text)))


;; TODO board delete line adds to recovery drawer
(re-frame/reg-event-db
 ::delete-line
 [->localStorage]
 (fn [db [_ text]]
   (update db :lines (fn [lines] (filter #(not= text %) lines)))))

(defn day-of-year [^js/Date d]
  (let [year       (.getFullYear d)
        start      (js/Date. year 0 0)
        ms-per-day 86400000]
    (-> (- (.getTime d) (.getTime start))
        (/ ms-per-day)
        (Math/floor))))

(defn is-different-day?
  "Given two timestamps in milliseconds, return true if they are on different days."
  [first-timestamp second-timestamp]
  (if (or (nil? first-timestamp)
          (nil? second-timestamp))
    false ; only one day is accounted for
    (not= (day-of-year (js/Date. first-timestamp))
          (day-of-year (js/Date. second-timestamp)))))

(re-frame/reg-event-db
 ::reset-board
 [->localStorage]
 (fn [db _]
   (assoc db :lines [])))

(re-frame/reg-event-db
 ::check-day
 [->localStorage]
 (fn [db _]
   (when (is-different-day? (:last-used db) (js/Date.now))
     (re-frame/dispatch [::reset-board]))
   (assoc db :last-used (js/Date.now))))