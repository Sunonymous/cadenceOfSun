(ns cos.views.tools
  (:require [reagent.core :as r]))

(defn accordion
  "Passed a title string and hiccup for content, makes a collapsible section."
  [title content]
  (let [open? (r/atom false)]
    (fn [title content]
      [:div.accordion
       {:class (when @open? "open")}
       [:button.accordion-title
        {:class (when @open? "open")
         :on-click #(swap! open? not)}
        title]
       (when @open?
         [:div.accordion-content
          content])])))

(def tools
  {:keer   {:name        "Keer"
            :description "help practice a musical motion in all twelve keys."
            :url         "/#/keer"
            :children    [:ul {:style {:list-style-type :disc
                                       :padding-left "1.5em"}}
                          [:li [accordion "who might find this useful?"
                                [:p "A musician who is working to practice playing an instrument efficiently."]]]
                          [:li [accordion "why did I make this?"
                                [:p "I've already made some other tools to practice music, and yet I noticed that I still did not learn or practice music in all twelve keys. I figured that perhaps it was because I was yet to have a simple way to walk through all twelve musical keys (I tend to get musically sidetracked)."]]]
                          [:li [accordion "what could be improved?"
                                [:p "Adding a feature to automatically cycle through the keys after a certain amount of time has passed."]]]
                          ]}
   :tsc    {:name        "Tabcyclestack"
            :description "track many small, moving things. For those consistently keeping tabs on their life."
            :url         "https://tabcyclestack.surge.sh/"}
   :mpt    {:name        "Musical Practice Tools"
            :description "improvise with a musical instrument in a more systematic or new way."
            :url         "https://musicalpracticetools.com/"}
   :fibril {:name        "Fibril"
            :description "track yourself over time. A personal energy organizer."
            ;; :url         "https://www.dropyourmind.com/"
            }
   :flo    {:name        "CallFlo"
            :description "track per-call interactions for phone workers."
            :url         "https://callflo.app/"}})

(defn tool->li
  "Given a keyword reference to a tool, return a hiccuped list item."
  [t]
  [:li.content-box.half-screen ; remember to attach key!
   {:key (-> tools t :name)}
   (if (-> tools t :url)
     [:a {:href (-> tools t :url)
          :style {:text-decoration "underline"}}
      [:h3.text-lg (-> tools t :name)]]
     [:h3.text-lg (-> tools t :name)])
   [:p "— a tool to " (-> tools t :description)]
   (if-let [children (-> tools t :children)]
     children
     nil)])

(defn tools-list []
  [:div
   [:ul
    (doall
     (for [tool (keys tools)]
       ^{:key tool} [tool->li tool]))
    ]])

(defn main []
    [:div.p-4
     {:class ["width-full" :p-2 "rounded-[5px]"]}
     [:h2.text-4xl.thick "· tools ·"]
     [:p.m-4 "maybe these can help"]
     [tools-list]
    ])