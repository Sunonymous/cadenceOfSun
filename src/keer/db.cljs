(ns keer.db
  (:require
   [keer.util :refer [build-sequence]]))

(def default-db
  {:use-sharps?         false
   :tone-sequence      (build-sequence 0 7)
   :semitone-adjustment 7 ;; should match last argument of line above
   :starting-index      0
   :previous-tone       nil
   :previous-lap        nil
   :laps                0})
