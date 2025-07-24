(ns cos.routes
  (:require
    [re-frame.core          :as rf]
    [reitit.frontend        :as rtf]
    [reitit.frontend.easy   :as rtfe]
    [reitit.coercion.schema :as rsc]
    [cos.views.home         :as home]
    [cos.views.about        :as about]
    [cos.views.now          :as now]
    [cos.views.tools        :as tools]
    [cos.views.music        :as music]
    [cos.views.works        :as works]
    [cos.views.connect      :as connect]
    [keer.views             :as keer]
    [kitchen.core           :as kitchen]
    [tools.reframetools     :refer [sdb gdb]]))

;;https://clojure.org/guides/weird_characters#__code_code_var_quote
(def routes
    (rtf/router
      ["/"
       [""
        {:name :routes/#frontpage :view #'home/main}]
       ["now"
        {:name :routes/#now       :view #'now/main}]
       ["about"
        {:name :routes/#about     :view #'about/main}]
       ["tools"
        {:name :routes/#tools     :view #'tools/main}]
       ["works"
        {:name :routes/#works     :view #'works/main}]
       ["music"
        {:name :routes/#music     :view #'music/main}]
       ["connect"
        {:name :routes/#connect   :view #'connect/main}]
       ["keer"
        {:name :routes/#keer     :view #'keer/main}]
       ["kitchen"
        {:name :routes/#kitchen     :view #'kitchen/main}]
      ]

      {:data {:coercion rsc/coercion}}))

(defn on-navigate [new-match]
  (when new-match
    (rf/dispatch [:routes/navigated new-match])))

(defn app-routes []
  (rtfe/start! routes
               on-navigate
               {:use-fragment true}))

(rf/reg-sub
 :routes/current-route
 (gdb [:current-route]))

;;; Events
(rf/reg-event-db
 :routes/navigated
 (sdb [:current-route]))

(rf/reg-event-fx
 :routes/navigate
 (fn [_cofx [_ & route]]
   {:routes/navigate! route}))


