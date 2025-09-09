(ns ^:figwheel-hooks cos.core
  (:require
   [reagent.dom.client       :as rdc]
   [cos.events               :as events]
   [cos.styles               :as styl]
   [goog.dom                 :as gdom]
   [cos.views.home           :as views]
   [react                    :as react]
   [cos.config               :as config]
   [cos.routes               :as routes]
   [re-frame.core            :as re-frame]
   [board.events             :as board.events]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defonce root (rdc/create-root (gdom/getElement "app")))

(defn mount-root []
  (println "mount")
  (re-frame/clear-subscription-cache!)
  (styl/inject-trace-styles js/document)
  (rdc/render root [:> react/StrictMode {} [#'views/main-panel]]))

(defn ^:after-load re-render []
  (mount-root))

(defn ^:export init []
  (println "init again..")
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (re-frame/dispatch [::board.events/check-day]) ; reset board if a new day
  (routes/app-routes)

  (mount-root))