(ns pantry.core
    (:require [reagent.core :as r]
              [re-frame.core :as re-frame]
              [pantry.subs :as subs]
              [pantry.events :as events]))

(defn name-editor [food-name]
  (let [potential-name (r/atom food-name)]
    (fn []
      (let [food @(re-frame/subscribe [::subs/food food-name])]
        [:div.pantry-row
         [:label.pantry-description "Food Name: "]
         [:input {:type        :text
                  :value       (or @potential-name (:name food))
                  :placeholder "Food Name"
                  :on-blur     #(when (not= @potential-name food-name)
                                  (do
                                    (re-frame/dispatch-sync [::events/update-food-name food-name @potential-name])
                                    (re-frame/dispatch-sync [::events/target-food @potential-name])
                                    ))
                  :on-change   #(reset! potential-name (-> % .-target .-value))}]])
      )))

(defn category-editor [food-name]
  (let [potential-category (r/atom @(re-frame/subscribe [::subs/food-category food-name]))]
    (fn [food-name]
      (let [food @(re-frame/subscribe [::subs/food food-name])]
        [:div.pantry-row
         [:label.pantry-description "Category: "]
         [:input {:type        :text
                  :value       (or @potential-category (:category food))
                  :placeholder "Category"
                  :on-blur     #(when (not= @potential-category (:category food))
                                  (re-frame/dispatch [::events/update-food-category food-name @potential-category]))
                  :on-change   #(reset! potential-category (-> % .-target .-value))}]]))))

(defn image-editor
  [food-name]
  (let [potential-url (r/atom @(re-frame/subscribe [::subs/food-img-url food-name]))]
    (fn [food-name]
      (let [food @(re-frame/subscribe [::subs/food food-name])]
        [:<>
         [:div.pantry-row
          [:label.pantry-description "Image URL: "]
          [:input {:type        :text
                   :value       (or @potential-url (:img food))
                   :placeholder "Image URL"
                   :on-blur     #(when (not= @potential-url (:img food))
                                   (re-frame/dispatch [::events/update-food-image food-name @potential-url]))
                   :on-change   #(reset! potential-url (-> % .-target .-value))}]
          [:button
           {:style {:all :revert}
            :on-click #(js/window.open
                        (str "http://www.google.com/images?q=" food-name) "_blank")}
           "🔎"]]
         [:div {:style {:padding "1em"
                        :gap "1em"}}
          [:img.food-thumbnail.pantry.fade-in-slow {:src (:img food)}]
          ]])
      )))

(defn remove-food-button [food-name]
  [:button
   {:style {:all           :revert
            :width         :fit-content
            :font-size     :1.25em
            :margin-inline :auto}
    :on-click #(re-frame/dispatch [::events/remove-food food-name])}
   "Remove Food 🗑️"])

; passed a food object, naturally
(defn food-viewer [{:keys [name category img]}]
  (let [open? (= name @(re-frame/subscribe [::subs/target-food]))]
    [:li
     {:id name
      :style {:display        :flex
              :flex-direction :column
              :padding    "1px 0.5em"
              :border-top "1px solid black"
              :background (if open? "lightgray" "white")}}
     [:span.food-name
      {:class (when open? "targeted")
       :on-click #(if open?
                    (re-frame/dispatch [::events/clear-target-food])
                    (re-frame/dispatch [::events/target-food name]))}
      name]
     (when open?
       [:div.fade-in
        {:style {:padding "1em"
                 :display        :flex
                 :flex-direction :column
                 :gap            "0.5em"}}
        [name-editor        name]
        [category-editor    name]
        [image-editor       name]
        [remove-food-button name]])]))

(defn add-new-food-button []
  (let [prompt-name! #(let [name (js/prompt "Food Name")]
                        (when (seq (.trim name))
                          (re-frame/dispatch [::events/add-new-blank-food name])
                          (re-frame/dispatch [::events/target-food name])))]
    [:button
     {:style {:all :revert}
      :on-click #(if (seq @(re-frame/subscribe [::subs/foods]))
                   (prompt-name!)
                   (re-frame/dispatch [::events/add-example-foods])
                 )}
     "Add Food"]))

;; TODO unsure if this is good idea. idea is -> permanent section
;; at top of bottom for adding food, because it is a core functionality
;; maybe it could be collapsible, and when collapsed, the button is the addition symbol
(defn new-food-adder
  []
  (let []
    (fn []
      [:div
       {}
       ])))

(defn main []
  (let [foods @(re-frame/subscribe [::subs/foods])]
    [:div
     {:style {:display        :flex
              :flex-direction :column
              :align-items    :flex-start
              :max-width      :800px
              :border-left    "4px solid black"
              :padding-left   :1em
              :margin         "0 auto"}}
     [:h1 {:style {:font-size :3rem}} "Pantry"]
     [:div
      {}
      [:div
       {:style {:margin-block "1em"
                :padding-inline "1em"
                :display :flex
                :justify-content :space-between}}
       [add-new-food-button]
       [:p "Head back to the "
        [:a {:href "/#/kitchen"
             :style {:text-decoration :underline
                     :cursor          :pointer}}
         "kitchen"] "?"]]

      (if (seq foods)
        [:ul
         {:style {:display        :flex
                  :flex-direction :column
                  :gap            "0"}}
         (doall
          (for [[food-name food] (sort foods)]
            ^{:key food-name}
            [food-viewer food]))]
        [:div
         [:p
          {:style {:text-align :center :font-size :1.5em}}
          [:span "🕸️"]
          [:br]
          [:span "️🕷️"]
          [:br]
          "Your virtual pantry seems empty..."]])]]))