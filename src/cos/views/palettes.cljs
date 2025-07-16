(ns cos.views.palettes
  (:require [reagent.core :as r]
            [reagent.dom :as dom]
            [re-frame.core :as re-frame]
            [clojure.string :refer [capitalize]]
            [cos.subs :as subs]))

;; Got this from ChatGPT
;; probably will delete after palette selection

;; --- Define palettes ---
(def palettes
  {
   :wheat {:bg "#F5F0E1" :text "#382F24" :accent "#D99058" :secondary "#B3A28F"}
   :clay  {:bg "#F4EFEA" :text "#3F2C1D" :accent "#A85D3A" :secondary "#CBB89D"}
   :grove {:bg "#FAF7F2" :text "#4B3621" :accent "#C2703D" :secondary "#9C8F7B"}
  })

;; --- State for the current palette ---
(defonce current-palette (r/atom (:clay palettes)))

;; --- Utility to set CSS variables on the root element ---
(defn apply-palette! [palette]
  (doseq [[k v] palette]
    (.setProperty (.-style (.-documentElement js/document))
                  (str "--" (name k))
                  v)))

;; --- Component ---
(defn palette-toggle []
  (let [set-theme! (fn [k]
                     (let [p (palettes k)]
                       (reset! current-palette p)
                       (apply-palette! p)))]
    (r/create-class
     {:component-did-mount #(apply-palette! @(re-frame/subscribe [:subs/palette-index]))
      :reagent-render
      (fn []
        [:div {:style {:font-family "sans-serif"
                       :background "var(--bg)"
                       :color "var(--text)"
                       :padding "2rem"
                       :position :relative
                       :transition "background 0.3s, color 0.3s"}}
         [:h1.thick {:style {:font-size :1.5rem :color "var(--accent)"}} "Warm, Earthy Palette Toggle"]
         [:p "Select a palette below to preview:"]
         (for [[k _] palettes]
           ^{:key k}
           [:button
            {:on-click #(set-theme! k)
             :style {:background "var(--secondary)"
                     :color "var(--text)"
                     :border "none"
                     :padding "0.5rem 1rem"
                     :margin "0.25rem"
                     :border-radius "0.5rem"
                     :cursor "pointer"
                     :font-weight "bold"
                     :transition "background 0.2s"}}
            (capitalize (name k))])
         [:div {:style {:border "2px solid var(--secondary)"
                        :background-color "white"
                        :padding "1rem"
                        :margin-top "1rem"
                        :border-radius "1rem"}}
          [:p "This section uses the background, text, "
           [:span.accent "accent, "]
           "and secondary colors in context. It’s meant to demonstrate legibility and aesthetic balance in a soft, grounded theme."]]])})))

;; --- Entry point ---
(defn ^:dev/after-load init []
  (dom/render [palette-toggle]
              (.getElementById js/document "app")))
