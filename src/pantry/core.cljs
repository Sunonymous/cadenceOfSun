(ns pantry.core
    (:require [reagent.core :as r]
              [re-frame.core :as re-frame]
              [pantry.subs :as subs]
              [pantry.events :as events]
              [tools.viewtools :refer [simple-link]]))

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

(defn nickname-editor [food-name]
  (let [potential-nickname (r/atom @(re-frame/subscribe [::subs/food-nickname food-name]))]
    (fn [food-name]
      (let [food @(re-frame/subscribe [::subs/food food-name])]
        [:div.pantry-row
         [:label.pantry-description "Nickname: "]
         [:input {:type        :text
                  :value       (or @potential-nickname (:nickname food))
                  :placeholder "(shown to diner)"
                  :on-blur     #(when (not= @potential-nickname (:nickname food))
                                  (re-frame/dispatch [::events/update-food-nickname food-name @potential-nickname]))
                  :on-change   #(reset! potential-nickname (-> % .-target .-value))}]]))))

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
      :style {:width         "100%"
              :display        :flex
              :flex-direction :column
              :padding-top    "1px"
              :padding-bottom "1px"
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
        {:style {:padding-left   "0.75em"
                 :padding-bottom "0.5em"
                 :display        :flex
                 :flex-direction :column
                 :gap            "0.5em"}}
        [name-editor        name]
        [nickname-editor    name]
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
     "➕ Add Food"]))

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

(defn data-export-button []
  (let [foods @(re-frame/subscribe [::subs/foods])]
    [:button
     {:style {:all :revert
              ;; :margin "2em 0.5em"
              :padding-inline "0.5em"}
      :on-click #(re-frame/dispatch [::events/export-pantry-foods])}
     "Save Data to File"]))

(defn data-import-button []
  [:div
   [:input {:id "uploadInput" :style {:display "none"}
            :type "file" :accept ".edn"
            :on-change (fn [e] (let [reader (js/FileReader.)
                                     file   (-> e .-target .-files (aget 0))]
                                 (set! (.-onload reader)
                                       (fn [e]
                                         (let [result (-> e .-target .-result)]
                                           (re-frame/dispatch [::events/load-foods-from-string result]))))
                                 (-> reader (.readAsText file))))}]
   [:button
    {:style {:all :revert}
     :on-click #(-> (js/document.getElementById "uploadInput") .click)
     :aria-label "load user data from file"}
    "Load Data from File"]])

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
      {:style {:width "100%"}}
      [:div
       {:style {:margin-block "1em"
                :padding-inline "0.5em"
                :display :flex
                :justify-content :space-between
                :align-items :center}}
       [add-new-food-button]
       [:p "Head back to the "
        [simple-link :routes/#kitchen "kitchen"]
         "?"]]

      (if (seq foods)
        [:div
         [:ul
          {:style {:display        :flex
                   :flex-direction :column
                   :gap            "0"}}
          (doall
           (for [[food-name food] (sort foods)]
             ^{:key food-name}
             [food-viewer food]))]
         [:div
          {:style {:background-color :gray
                   :display :flex
                   :justify-content :space-around
                   :align-items :center
                   :padding "0.5em 0.25em"}}
          [data-export-button]
          [data-import-button]
          [:button
           {:style {:all :revert}
            :on-click #(when (js/confirm "This removes all foods from your pantry. Are you sure? (refresh the page immediately if done on accident)")
                         (re-frame/dispatch [::events/clear-pantry]))}
           "Empty Pantry"]]]
        [:div
         [:p
          {:style {:text-align :center :font-size :1.5em}}
          [:span "🕸️"]
          [:br]
          [:span "️🕷️"]
          [:br]
          "Your virtual pantry seems empty..."]])]]))