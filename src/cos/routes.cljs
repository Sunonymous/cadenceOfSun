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
    [pantry.core            :as pantry]
    [board.core             :as board]
    [cos.events             :as events]
    [tools.util             :refer [one-of]]
    [tools.reframetools     :refer [sdb gdb]]))

;;https://clojure.org/guides/weird_characters#__code_code_var_quote

; !!!
;; Remember to add page-title below when adding a new route.

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
        {:name :routes/#keer      :view #'keer/main}]
       ["kitchen"
        {:name :routes/#kitchen   :view #'kitchen/main}]
       ["pantry"
        {:name :routes/#pantry    :view #'pantry/main}]
       ["board"
        {:name :routes/#board     :view #'board/main}]
      ]

      {:data {:coercion rsc/coercion}}))

;; TODO add variations to page titles, subtle or not
;;; use one-of function and pass a collection of strings
(defn route->page-title [route]
  (get {:routes/#frontpage (one-of ["Nice to meet you!" "Good day!" "Hello there!"])
        :routes/#now       (str "A " (one-of ["Brief Timeline" "Tiny Snapshot" "Miniscule Filter"]) " of the Eternal Moment")
        :routes/#about     "A Few Words"
        :routes/#tools     "Personal and Peculiar Tools"
        :routes/#works     "Works"
        :routes/#music     "My Sonic Manglings"
        :routes/#connect   "Let's chat!"
        :routes/#keer      "Practice Practice Practice!"
        :routes/#kitchen   "Kitchen"
        :routes/#pantry    "Pantry"}
       route
       "<?>"))

(defn on-navigate [new-match]
  (when new-match
    (let [route      (-> new-match :data :name)
          page-title (route->page-title route)]
      (when page-title
        (rf/dispatch [::events/set-page-title (str page-title " · Cadence of Sun ·")]))
      (rf/dispatch [:routes/navigated new-match]))))

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


