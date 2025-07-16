(ns keer.util
  (:require [keer.data :refer [tones-with-flats tones-with-sharps]]))

(defn build-sequence [start-index delta]
  ;; Let's create a fallback, just in case a bad index is passed in.
  (if (not (<= 0 start-index 11))
    nil ;; if we have a bad start, we stop playing
    (loop [sequence [start-index]] ;; otherwise, we recurse
      (if (= (count sequence) 12) ;; when we reach the length we want, we're done
        sequence ;; ta-da!
        ;; otherwise, add the next index by modulating the (final index + delta) by the total number of keys
        (recur (conj sequence (mod (+ (last sequence) delta) 12)))))))

(defn display-key
  [i sharps?]
  (when i
    (nth (if sharps? tones-with-sharps tones-with-flats) i))
)