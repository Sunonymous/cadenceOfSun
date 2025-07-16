(ns tools.reframetools)

(defn sdb [path] ; store-db, I guess
  (fn [db [_ v]]
    (assoc-in db path v)))

(defn gdb ; get-db, I guess
  [path]
  (fn [db _] (get-in db path)))