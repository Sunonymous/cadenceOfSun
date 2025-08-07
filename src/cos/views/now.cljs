(ns cos.views.now
  (:require [tools.viewtools :refer [simple-link]]))

(defn month-of-activity
  "Month and year are numbers or strings. Children is a list of hiccup.
   Probably should be filled with :li elements."
  [month year children]
  [:article.content-box.thin
   [:h3.text-3xl.thick (str month " ") [:span.accent (str year)] "—"]
   [:ul
    {:style {:list-style-type :disc
             :padding-left "1em"}}
    children]])

(defn main []
  [:div.p-4
   [:h2.text-4xl.thick "· now ·"]

   [month-of-activity "August" 2025
    [:<>
     [:li "Released " [simple-link :routes/#kitchen "Kitchen+Pantry"]  " combination."]]]
   [month-of-activity "July" 2025
    [:<>
     [:li "Revisiting " [:a {:href "https://callflo.app/"} "CallFlo"]]
     [:li "Prototyping Stamps"]
     [:li "Got this website online ("
      [:a.anchor {:href "https://github.com/Sunonymous/cadenceOfSun"
           :target "_blank"
           :rel "noopener noreferrer"}
       "thanks GitHub!"]
      ")"]]]
   [month-of-activity "June" 2025
    [:li "Making this website"]
    [:li "Prototyping " [simple-link :routes/#keer "Keer"]]
    [:li "Exploring Stamps ideology"]]
   ])