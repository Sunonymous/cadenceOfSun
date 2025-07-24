(ns kitchen.core
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame]
            [kitchen.events :as events]
            [kitchen.subs :as subs]
            [kitchen.data :refer [foods]]))

;; TODO strip foods for categories and make "show all ___ " buttons



(defn food-item [item is-included?]
  [:li
   {:style {:padding          "0.5em"
            :margin-block     "0.25em"
            :margin-inline    "0.5em"
            :border           "1px solid black"
            :background-color (if is-included? "lightgreen" "white")}
    :on-click #(re-frame/dispatch [::events/toggle-food-selection (:name item)])}
   ; (or (:nickname item) (:name item)) ; TODO do this for display to diner
   (:name item)])

(defn detailed-food-item [food-name]
  (let [food         (foods food-name)
        meal-order  @(re-frame/subscribe [::subs/meal-order])
        is-in-order? (boolean (meal-order food-name))]
    [:li
     {:style {:padding          "0.5em"
              :margin           "0.5em"
              :border           "1px solid black"
              :background-color (if is-in-order? "lightgreen" "white")
              :display          "flex"
              :justify-content  "space-between"
              :align-items      "center"}}
     [:div
      {:style {:display :flex :gap "1.5em"
               :align-items :center}}
      [:img.food-thumbnail {:src (:img food)}]
      [:span
       {:style {:font-size "1.5em"
                :display :inline-block}}
       (or (:nickname food) (:name food))]]
     [:input
      {:type       :checkbox
       :checked    is-in-order?
       :on-change #(re-frame/dispatch [::events/toggle-order-selection food-name])
       :style      {:margin-right "0.5em" :transform "scale(1.5)"}}]]))

(defn select-all-button
  []
  [:button
   {:on-click #(re-frame/dispatch [::events/offer-all-foods])
    :style {:all :revert}}
   "Select All"])

(defn deselect-all-button
  []
  [:button
   {:on-click #(re-frame/dispatch [::events/remove-all-offered-foods])
    :disabled (empty? @(re-frame/subscribe [::subs/selected-foods]))
    :style {:all :revert}}
   "Deselect All"])

(defn prev-stage-button
  []
  [:button
   {:on-click #(re-frame/dispatch [::events/previous-stage])
    :style {:all :revert}}
   "Prev Step ⬅️"])

;; previous stage is an admin function
;; but this button can be reused throughout nearly every stage
;; so I provided another arity that can have custom text
(defn next-stage-button
  ([]
   [next-stage-button "➡️ Next Step"])
  ([text]
   [:button
    {:on-click #(re-frame/dispatch [::events/next-stage])
     :style {:all :revert}}
    text]))

(defn food-offering-list [items]
  (let [selected-foods @(re-frame/subscribe [::subs/selected-foods])]
    [:div
     [:div
      {:style {:display :flex
               :justify-content :space-around}}
      [select-all-button]
      [deselect-all-button]
      [next-stage-button "Submit"]
     ]
     [:h3 {:style {:text-align :center :font-size "2em"}} "Choose Foods to Offer:"]
     [:ul
      (for [food items] ; checks for inclusion in already-selected foods
        ^{:key food}
        [food-item food (selected-foods (:name food))])]
      [next-stage-button "Submit"]
     ]))

(defn control-panel []
  [:div
   {:style {:margin-block "1em"
            :display :flex
            :justify-content :center
            :align-items :center
           }}
   [:span
    {:style {:font-size   "2rem"
             :position    :relative
             :line-height :1.5rem
             :user-select :none}
     :on-click #(re-frame/dispatch [::events/toggle-kitchen-controls])}
    "🧂"]
   (when @(re-frame/subscribe [::subs/show-kitchen-controls?])
     [:div
      [prev-stage-button]
      [next-stage-button]])])

;; Composites

(defn order-selection-menu
  [] ;; TODO Max number of items
     ;; TODO required, one of each category?
  (let [offered-foods @(re-frame/subscribe [::subs/selected-foods])
        meal-order    @(re-frame/subscribe [::subs/meal-order])]
    [:div
     {}
     [:h2
      {:style {:text-align :center :font-size "2.5em" :font-weight 700}}
      "Order Selection"]
     [:p
      {:style {:text-align :center :font-size "2em"}}
      "Choose the food you want to eat:"]
     [:ul
      (for [food offered-foods]
        ^{:key food}
        [detailed-food-item food])
      ]
     [:div
      {:style {:display :flex
               :justify-content :space-around}}
      [next-stage-button "Continue"]]
     ]))

(defn order-confirmation-prompt
  []
  (let [order (sort @(re-frame/subscribe [::subs/meal-order]))
        _ (js/console.log (str "order: " order))]
    [:div
     {}
     [:h2 {:style {:text-align :center :font-size "2em"}}
      "Confirm Your Order"]
     [:ul
      {:style {:padding-block "0.5em"
               :padding-inline "0.5em"
               :margin-block "0.5em"
               :margin-inline "0.5em"
               :font-size "1.5em"
               :border "1px solid black"
               :border-radius "8px"
               :background-color "white"}}
      (doall
       (for [food order]
         ^{:key food}
         [:li
          {:style {:margin-inline   "0.5em"
                   :padding-block   "0.5em"
                   :padding-inline  "0.5em"
                   :text-decoration "underline"
                   :display         "flex"
                   :align-items    "center"
                   :justify-content "space-between"
                   :gap             "1em"}}
          (or (-> foods (get food) :nickname) (-> foods (get food) :name))
          [:img.food-thumbnail.bigger {:src (-> foods (get food) :img)}]
          ]))]
     [:h3
      {:style {:text-align :center :font-size "1.5em"
               :margin-block "0.5em"}}
      "Is this what you want?"]
     [:div
      {:style {:display :flex
               :justify-content :center
               :gap "1em"}}
      [:button
       {:style {:all :revert :font-size "1.5em"}
        :on-click #(re-frame/dispatch [::events/next-stage])}
       "Yes"]
      [:button
       {:style {:all :revert :font-size "1.5em"}
        :on-click #(re-frame/dispatch [::events/previous-stage])}
       "No"]]]))

(defn main []
  [:div
   {:style {:max-width        :800px
            :margin-block     "1em"
            :margin-inline    :auto
            :padding          "1em"
            :border           "1px solid black"
            :border-radius    "1em"
            :background-color "white"}}
   [control-panel]
   (case @(re-frame/subscribe [::subs/kitchen-stage])
     :offer
     [food-offering-list (sort-by :name (vals foods))]

     :greet
     [:div
      {:style {:margin-block "1em"
               :text-align   :center}}
      [:h1 {:style {:text-align :center :font-size "2.5em"}} "Welcome to the Kitchen" ]
      [:p  {:style  {:text-align :center :font-size "1.5em"}} "We hope you're hungry!"]
      [:h2 {:style {:text-align :center :font-size "4.5em"
                    :margin-block "1em"}} "👩🏼‍🍳"]
      [:button {:style {:all :revert :font-size "1.5em"}
                :on-click #(re-frame/dispatch [::events/next-stage])} "Order Food"]
     ]

     :order
     [order-selection-menu]

     :confirm
     [order-confirmation-prompt]

     :prepare
     [:div
      {:style {:margin-block "4em"}}
      [:p.status-text
       {:style {:text-align :center :font-size "3em"}}
       "🔥"]
      [:div.cooking-animation
       [:div.dot]
       [:div.dot]
       [:div.dot]]
      [:p.status-text "Cooking in progress..."]
      ]

     :ready
     [:div {:style {:display :flex :flex-direction :column :align-items :center}}
      [:h1 {:style {:text-align :center
                    :font-size  "8em"}} "🛎️🍽️🧑‍🍳"]
      [:h2
       {:style {:margin-top    "2em"
                :margin-bottom "1em"
                :font-size     "2em"
                :text-align    :center}}
       "Your food is ready!"]
      [:p
       {:style {:margin-bottom "1em"
                :font-size     "1.5em"
                :text-align    :center}}
       "Wash your hands and come to the table."
       [:br]
       "Thanks for ordering with us!"
      ]
      [:button {:style {:all :revert :font-size "1.5em"}
                :on-click #((re-frame/dispatch [::events/next-stage])
                            (re-frame/dispatch [::events/next-stage])
                            (re-frame/dispatch [::events/clear-order]))}
       "You're welcome! Bye!"]
     ])
   ])