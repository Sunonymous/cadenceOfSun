(ns cos.views.works
  (:require [reagent.core :as r]))

(defn collapsible-section [title content]
  (let [content-open? (r/atom false)]
    (fn [title content]
      [:div.content-box.half-screen
       [:div.content-header
         {:on-click #(swap! content-open? not)
          :style {:display :flex :justify-content :space-between
                  :align-items :center :cursor :pointer
                  }}
        [:h3.content-title.thick
         {:style {:text-decoration (if @content-open?
                                     "underline" "none")} }
         title]
        [:span.content-title.thick
         (if @content-open?
           "-" "+")]]
       (when @content-open? content)])))

(defn organization-project []
  [collapsible-section "Organizing my Life"
   [:ul
    {:style {:list-style-type :disc
             :padding-left "1em"}}
    [:li "For as long as I can remember, there's been a consistent effort to organize my life."]
    [:li ""]
    [:li ]
   ]])

(defn mastery-project []
  [collapsible-section "Approaching Mastery and Education"
   [:ul
    {:style {:list-style-type :disc
             :padding-left "1em"}}
    [:li "Believe it or not, I play the piano."]
    [:li ]
   ]])

(defn main []
    [:div.p-4
     {:class ["width-full" :p-2 "rounded-[5px]"]}
     [:h2.text-4xl.thick "· works ·"]
     [organization-project]
    ])