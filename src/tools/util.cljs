(ns tools.util)

(defn inc-wrap-index
  "Given a collection and an index, return the next index, wrapping around to the beginning if needed."
  [coll i]
  (mod (inc i) (count coll)))

(defn copy-to-clipboard [text]
  (-> js/navigator
      .-clipboard
      (.writeText text)
      (.then #(js/console.log "Copied to clipboard!"))
      (.catch #(js/console.error "Failed to copy to clipboard:" %))))
