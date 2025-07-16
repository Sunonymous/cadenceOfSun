(ns cos.views.connect
  (:require
   [reagent.core :as r]
   [tools.util :refer [copy-to-clipboard]]))

;; TODO add skippable high-low-buffalo game
;; TODO obfuscate email

(defn personal-description []
  [:p "I'm Sunny. ☀️ "])

(defn contact-form
  []
  (let [hlb?       (reagent.core/atom false)
        gratitude? (reagent.core/atom false)]
    (fn []
      [:form
       [:label "Email"]
       [:input]
       [:label "Message"]
       [:textarea]
       [:button "Submit"]
      ])))

(defn main []
    [:div.p-4
     {:class ["width-full" :p-2 "rounded-[5px]"]}
     [:h2.text-4xl.thick "· connect ·"]
     [:p.my-4 "I am happy to hear from you. Left-click for mail app. Right-click for copy."]
     [:p.my-4.content-box.thin
      [:a {:href "mailto:embodiedsunshine@gmail.com"
           :style {:cursor :pointer
                   :font-size :1.25rem
                   :font-weight :bold
                   :font-family "Playfair Display, serif"}
           :on-context-menu (fn [e] (.preventDefault e)
                              (copy-to-clipboard "embodiedsunshine@gmail.com"))}
       "📧 Email me!"]]
    ])