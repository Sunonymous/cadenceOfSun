(ns cos.views.now)

(defn main []
  [:div.p-4
   [:h2.text-4xl.thick "· now ·"]

   ;; TODO make this into a function
   [:article.content-box.thin
    [:h3.text-3xl.thick "July " [:span.accent "2025"] "—"]
    [:ul
     {:style {:list-style-type :disc
              :padding-left "1em"}}
     [:li "Revisiting " [:a {:href "https://callflo.app/"} "CallFlo"]]
     [:li "Prototyping Stamps"]
     [:li "Got this website online ("
      [:a {:href "https://github.com/Sunonymous/cadenceOfSun"
           :target "_blank"
           :rel "noopener noreferrer"}
       "thanks GitHub!"]
      ")"]]]
   [:article.content-box.thin
    [:h3.text-3xl.thick "June " [:span.accent "2025"] "—"]
    [:ul
     {:style {:list-style-type :disc
              :padding-left "1em"}}
     [:li "Making this website"]
     [:li "Prototyping " [:a {:href "/#/keer"} "Keer"]]
     [:li "Exploring Stamps ideology"]]]])