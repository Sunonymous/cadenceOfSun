(ns cos.subs
  (:require [re-frame.core :as re-frame]
            [tools.reframetools :refer [gdb]]))

(re-frame/reg-sub ::name                (gdb [:name]))
(re-frame/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))
(re-frame/reg-sub ::palette-index       (gdb [:palette-index]))
