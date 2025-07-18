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
            :url         "#/keer"
            :children    [:ul {:style {:list-style-type :disc
                                       :padding-left "1.5em"}}
                          [:li [accordion "Who might find this useful?"
                                [:p "A musician who is working to practice playing an instrument efficiently."]]]
                          [:li [accordion "Why did I make this?"
                                [:p "I've already made some other tools to practice music, and yet I noticed that I still did not learn or practice music in all twelve keys. I figured that perhaps it was because I was yet to have a simple way to walk through all twelve musical keys (I tend to get musically sidetracked)."]]]
                          [:li [accordion "What could be improved?"
                                [:p "Adding a feature to automatically cycle through the keys after a certain amount of time has passed."]]]
                          ]}
   :tsc    {:name        "Tabcyclestack"
            :description "track many small, moving things. For those consistently keeping tabs on their life."
            :url         "https://tabcyclestack.surge.sh/"
            :children    [:ul {:style {:list-style-type :disc
                                       :padding-left "1.5em"}}
                          [:li [accordion "What's a tab and why would I want to cyclestack it?"
                                [:p "A tab is a small string of text related to anything you want to hold in your mind. Stacks are collections of tabs and you probably cycle through them."]]]
                          [:li [accordion "What's unique about this tool?"
                                [:p "I went with a minimalistic start for the design, and it looks nice in its simplicity. I played with a bit of frill too. Looks best (as it can) on larger screens."]]]
                          [:li [accordion "Any regets??"
                                [:p "Never remaking the piles interface. The most unusual feature of the application was that you could create dynamic views of your tabs and stacks. I had an interface, though it needed improvement, and that never happened. The same idea is translating into the Stamps application."]]]
                          ]}
   :mpt    {:name        "Musical Practice Tools"
            :description "improvise with a musical instrument in a more systematic or new way."
            :url         "https://musicalpracticetools.com/"
            :children    [:ul {:style {:list-style-type :disc
                                       :padding-left "1.5em"}}
                          [:li [accordion "How many times have you rebuilt this?"
                                [:p "Twice. Once when I didn't know what I was doing, and then later when I knew that I don't know what I am doing."]]]
                          [:li [accordion "Do you seriously use this all the time?"
                                [:p "Actually, I do use this regularly, though I only use one or two of its primary features."]]]
                          [:li [accordion "Has this project received any acclaim?"
                                [:p "Nope. I still like it though."]]]
                          ]}
   :minddrop {:name        "Minddrop"
              :description "track yourself over time. A personal energy organizer."
              :url         "https:/dropyourmind.com/"
              :children    [:ul {:style {:list-style-type :disc
                                         :padding-left "1.5em"}}
                            [:li [accordion "What kind of a name is that?"
                                  [:p "It went with the mental model of the program. I've learned over time (and error) to avoid letting that bleed into the actual verbiage of the code itself. Ultimately the idea was that the mind is an ocean of information, with each drop capable of containing more drops."]]]
                            [:li [accordion "Where did this one go wrong?"
                                  [:p "I didn't like the level of friction that I felt creating, navigating, and manipulating the drops. It was too much effort to be worth it."]]]
                            [:li [accordion "I guess you could say your mind dropped this project?"
                                  [:p "Yes, and that's not a question."]]]]}
   :flo    {:name        "CallFlo"
            :description "track per-call interactions for phone workers."
            :url         "https://callflo.app/"
            :children    [:ul {:style {:list-style-type :disc
                                       :padding-left "1.5em"}}
                          [:li [accordion "Who is this useful for?"
                                [:p "People that do work that goes in cycles. Doesn't necessarily have to be phone work, though that's how it started."]]]
                          [:li [accordion "What does it do?"
                                [:p "Let's you track a (customizable) list of behaviors and provides you statistics for your performance."]]]
                          [:li [accordion "What about my private data??"
                                [:p "It is still yours and private. What happens in localStorage stays in localStorage."]]]
                          ]}})

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
     [:p.m-4 "something for you and me"]
     [tools-list]
    ])