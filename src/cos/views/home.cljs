(ns cos.views.home
  (:require
   [re-frame.core :as re-frame]
   [cos.events :as events]
   [tools.viewtools :as vt]
   [tools.util :refer [inc-wrap-index]]
   [cljs.pprint :as pp]
   [cos.db :refer [palettes]]
   [cos.subs :as subs]
   ))

(def route-debug? false)

(defn main []
  [:div.p-4
   {:class ["width-full" :p-2 "rounded-[5px]"]}
   [:h2.text-4xl.thick "· cadence of " [:span.accent "sun"] " ·"]
   [:p.my-4 "I'm Sunny ☀️—a maker, parent, and lifelong learner with a deep interest in human systems, clear design, and personal mastery. I create thoughtful tools for practice and self-exploration. My background bridges technical implementation with people-focused design, and I care most about work that empowers, clarifies, or uplifts."]
  ])

(def toolbar-items
  [
   ["now"         :routes/#now]
  ;;  ["works"       :routes/#works]
   ["tools"       :routes/#tools]
  ;;  ["music"       :routes/#music]
  ;;  ["about"       :routes/#about]
   ["connect"     :routes/#connect]
  ])

(defn route-info [route]
  [:div.m-4
   [:p "Routeinfo"]
   [:pre.border-solid.border-2.rounded
    (with-out-str (pp/pprint route))]])
;; main

(defn show-panel [route]
  (when-let [route-data (:data route)]
    (let [view (:view route-data)]
      [:<>
       [view]
       (when route-debug?
         [route-info route])
       ])))

(defn main-panel []
  (let [active-route (re-frame/subscribe [:routes/current-route])]
    [:div
     [vt/navigation toolbar-items]
     [show-panel @active-route]
    ]))
