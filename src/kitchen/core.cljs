(ns kitchen.core
  (:require [reagent.core    :as    r]
            [clojure.string  :as    str]
            [kitchen.subs    :as    subs]
            [kitchen.events  :as    events]
            [re-frame.core   :as    re-frame]
            [pantry.subs     :as    pantry-subs]
            [tools.viewtools :as    vt]))

;; TODO adjust de/select all buttons to only work with filtered items

(defn food-item [item is-included?]
  [:li
   {:style {:padding          "0.5em"
            :margin-block     "0.25em"
            :margin-inline    "0.5em"
            :border           "1px solid black"
            :background-color (if is-included? "lightgreen" "white")}
    :on-click #(re-frame/dispatch [::events/toggle-food-selection (:name item)])}
   (:name item)])

(defn detailed-food-item [food-name can-select?]
  ;; can-select? is there to only allow the child to select a number of foods
  (let [foods        @(re-frame/subscribe [::pantry-subs/foods])
        food         (foods food-name)
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
     (when (or can-select? is-in-order?)
       [:input
        {:type       :checkbox
         :checked    is-in-order?
         :on-change #(re-frame/dispatch [::events/toggle-order-selection food-name])
         :style      {:margin-right "0.5em" :transform "scale(1.5)"}}])]))

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
   "Reset"])

(defn prev-stage-button
  []
  [:button
   {:on-click #(re-frame/dispatch [::events/previous-stage])
    :style {:all         :revert
             :padding     "0.5em 0.5em"
             :font-weight 600}}
   "⬅️"])

;; previous stage is an admin function
;; but this button can be reused throughout nearly every stage
;; so I provided another arity that can have custom text
(defn next-stage-button
  ([]
   [next-stage-button "➡️"])
  ([text]
   [:button
    {:on-click #(re-frame/dispatch [::events/next-stage])
     :style {:all         :revert
             :padding     "0.5em 0.5em"
             :font-weight 600}}
    text]))

(defn food-offering-list [items]
  (let [categories        @(re-frame/subscribe [::subs/food-categories])
        cat-filter         (r/atom "") ; empty string means show all
        active-preset      (r/atom nil)]
    (fn [items]
      (let [presets @(re-frame/subscribe [::subs/kitchen-presets])
            selected-foods @(re-frame/subscribe [::subs/selected-foods])
            filtered-items (if (= @cat-filter "")
                             items
                             (filter #(= (:category %) @cat-filter) items))]
        [:div
         [:div
          {:style {:display         :flex
                   :justify-content :space-around
                   :align-items     :center}}
          #_(when (empty? @cat-filter)
            [select-all-button]) ; references hardcoded data
          (when (empty? @cat-filter)
            [deselect-all-button])
          (when (seq presets)
            [:div
             [:select
              {:on-change (fn [e] (let [preset (-> e .-target .-value)]
                                    (reset! active-preset preset)
                                    (if (= preset "")
                                      (reset! active-preset nil)
                                      (re-frame/dispatch [::events/load-offer-preset preset]))))}
              [:option {:value ""} "Load Preset"]
              (doall
               (for [preset (keys presets)]
                 ^{:key preset}
                 [:option {:value preset} preset]))]
             (when @active-preset
               [:button
                {:on-click (fn [_]
                             (re-frame/dispatch [::events/delete-offer-preset @active-preset])
                             (reset! active-preset nil))
                 :style {:all :revert :margin-left "0.5em"}
                 :disabled (not @active-preset)}
                "🗑️"])])
          [:button
           {:disabled (empty? selected-foods)
            :on-click (fn [_]
                        (let [proposed-name? (.trim (js/prompt "Preset name?"))]
                          (when (seq proposed-name?)
                            (re-frame/dispatch [::events/save-offer-preset proposed-name? selected-foods]))))
            :style {:all :revert}}
           "Save Preset"]
          (when (seq categories)
            [:div
             [:p "Show "]
             [:select
              {:on-change #(reset! cat-filter (.-value (.-target %)))}
              [:option {:value ""} "All"]
              (doall
               (for [category categories]
                 ^{:key category}
                 [:option {:value category} (str/capitalize category)]))]])
            (when (seq filtered-items)
              [next-stage-button "Submit"])]
         ;; TODO create display when there are no foods and encourage visiting pantry
         (if (seq filtered-items)
           [:div
            [:h3 {:style {:text-align :center :font-size "2em"}} "Choose Foods to Offer:"]
            [:ul
             (doall
              (for [food filtered-items] ; checks for inclusion in already-selected foods
                ^{:key food}
                [food-item food (selected-foods (:name food))]))]
            [:div
             {:style {:margin-block :1em
                      :display :flex
                      :justify-content :space-around}}
             [next-stage-button "Submit"]]]
           [:div ;; no foods in pantry
            {:style {:margin-block :2em}}
            [:h3 {:style {:text-align :center :font-size "2em"}} "I'd like to open the kitchen, but..."]
            [:h4 {:style {:text-align :center :font-size "1.75em"}} "My " [vt/simple-link :routes/#pantry "pantry"] " is empty. 😮‍💨"]
            ])]))))

(defn stage-display []
  (let [stages events/stages
        current-stage @(re-frame/subscribe [::subs/kitchen-stage])]
    [:div
     [:div
      {:style {:display :flex
               :justify-content :space-between
               :align-items :baseline
               :gap "0.5em"
               :margin-block :1em}}
      [:h3 {:style {:font-size "1.5em"
                    :margin-right :auto}}
       "Steps"]
      [prev-stage-button]
      [next-stage-button]]
     [:div
      {:style {;:margin :1.5em
               :padding "0 1em 0.5em 1em"
               :display :flex
               :justify-content :center
               :gap "0.5em"
               :border-bottom "2px solid black"}}
      (for [stage stages]
        ^{:key stage}
        [:button
         {:class (when (= stage current-stage) "active")
          :style {:all :revert
                  :cursor :pointer
                  :outline-offset :2px
                  :border-radius "0.5em"
                  :color (if (= stage current-stage)
                           "#000" "#999"
                  )
                  :border (if (= stage current-stage)
                            "2px solid black" "2px solid transparent"
                  )
                  ;; :outline (if (= stage current-stage)
                  ;;            "1px solid black" "none")
                  }
          :on-click #(re-frame/dispatch [::events/set-kitchen-stage stage])}
         (name stage)])]]))

(defn control-panel []
  (let [press-timeout-id (r/atom nil)
        start-timer!     (fn [_]
                           (reset! press-timeout-id
                                   (js/setTimeout #(re-frame/dispatch [::events/toggle-kitchen-controls]) 2000)))
        clear-timeout!  #(js/clearTimeout @press-timeout-id)]
    (fn []
      [:div
       {:style {:margin-block "1em"
                :display :flex
                :justify-content :center
                :align-items :center}}
       [:span.no-select
        {:style {:font-size   "2rem"
                 :position    :relative
                 :line-height :1.5rem
                 :user-select :none}
         :on-touch-start start-timer!
         :on-touch-end   clear-timeout!
         :on-mouse-leave clear-timeout!
         :on-mouse-up    clear-timeout!
         :on-mouse-down  start-timer!}
        "🧂"]
       [:div.control-panel
        {:class (when @(re-frame/subscribe [::subs/show-kitchen-controls?]) "open")}
        [stage-display]
        [:div
         {:style {:display :flex
                  :justify-content :center
                  :gap "1em"
                  :font-size "1.15em"
                  }}
         [:button ; close panel button
          {:style {:all :revert :font-size :inherit}
           :on-click #(re-frame/dispatch [::events/toggle-kitchen-controls])}
          "Close ⓧ"]
         [:button ; ready for child button
          {:style {:all :revert :font-size :inherit}
           :on-click #(do
                        (re-frame/dispatch [::events/toggle-kitchen-controls])
                        (re-frame/dispatch [::events/set-kitchen-stage :greet]))}
          "Ready ✓"]
         [:button ; reset kitchen button
          {:style {:all :revert :font-size :inherit}
           :on-click #(when (js/confirm "Are you sure you want to reset the kitchen? This clears the order and offered foods.")
                        (re-frame/dispatch [::events/reset-kitchen]))}
          "Reset ↺"]]
        [vt/simple-link :routes/#pantry "Go to Pantry"]
        ]])))

;; Composites

(defn order-selection-menu
  []
  (let [offered-foods @(re-frame/subscribe [::subs/selected-foods])
        max-items     (r/atom nil)]
    (fn []
      [:div
       (when @(re-frame/subscribe [::subs/show-kitchen-controls?])
         [:div
          [:label "Max # of Foods: "]
          [:input
           {:type       :number
            :min        1
            :max        (count offered-foods)
            :on-change  (fn [e] (let [num (js/parseInt (.-value (.-target e)))]
                                  (reset! max-items (if (js/isNaN num) nil num))))
            :value      @max-items
            :style      {:margin-left "0.5em" :border "1px solid black"}}
           ]
          ])
       [:h2
        {:style {:text-align :center :font-size "2.5em" :font-weight 700}}
        "Order Selection"]
       (if (seq offered-foods)
         [:div
          [:p
           {:style {:text-align :center :font-size "2em"}}
           "Choose the food you want to eat:"]
          (when @max-items
            (let [greater-than-1? (> @max-items 1)]
              [:h3 {:style {:text-align :center :font-size "1.5em"}}
               "Select " (when greater-than-1? "up to ") @max-items " item" (when greater-than-1? "s")]))
          [:ul
           (doall
            (for [food offered-foods]
              ^{:key food}
              [detailed-food-item food (or (nil? @max-items) ;; no maximum
                                           (< (count @(re-frame/subscribe [::subs/meal-order])) @max-items))]))]
          [:div
           {:style {:display :flex
                    :justify-content :space-around}}
           [next-stage-button "Continue"]]]
         [:div
          [:p
           {:style {:text-align :center :font-size "1em" :font-weight 600}}
           "No food is currently offered. Please check back later."]
          [:p
           {:style {:text-align :center :font-size "4em"}}
           "⚠️🤷🏻🫙"]])])))

(defn order-category-menu
  [offered-foods category]
  (let [foods @(re-frame/subscribe [::pantry-subs/foods])
        foods-in-category (filter #(= category (:category %)) (map (fn [f] (foods f)) offered-foods))]
    (fn [offered-foods category]
      (let [minimum       @(re-frame/subscribe [::subs/category-min category])
            maximum       @(re-frame/subscribe [::subs/category-max category])
            required?    (@(re-frame/subscribe [::subs/required-categories]) category)
            num-in-order  @(re-frame/subscribe [::subs/num-of-order-items-of-category category])
            valid-order?  (or (not required?)
                             (>= maximum num-in-order minimum))]
        [:div
         {:style {:border (str (if required? "3" "2") "px solid " (cond
                                                                    (and required? valid-order?)       "green"
                                                                    (and required? (not valid-order?)) "red"
                                                                    :otherwise                         "black"))
                  :border-radius "0.5em"
                  :margin "0.5em"
                  :padding "0.5em"}}
         [:div
          {:style {:display :flex :justify-content :flex-start :align-items :baseline :gap "0.5em"
                   }}
          [:h3 {:style {:font-size "1.5em"}}
           (if (or (nil? category)
                   (= "" category))
             "other" category) ":"] ;; TODO make this a configuration value?
          (when required?
            [:p
             (cond
               valid-order?
               (take 1 (shuffle ["Ready!" "Great choice!" "Got it!" "Delicious!" "Yum!"]))
               (= minimum maximum)
               (str "(Select " minimum " food" (when (> minimum 1) "s") ".)")
               :otherwise
               (str "(Select between " minimum " and " maximum " foods.)"))])]
         (when @(re-frame/subscribe [::subs/show-kitchen-controls?])
           [:div
            {:style {:display :flex :justify-content :center :align-items :baseline :gap "0.5em"}}
            [:label "Required? "
             [:input
              {:type       :checkbox
               :checked    (boolean required?)
               :on-change  (fn [e] (re-frame/dispatch [(if (.-checked (.-target e))
                                                         ::events/require-category-in-order
                                                         ::events/unrequire-category-in-order)
                                                       category]))
               :style      {:margin-left "0.5em" :border "1px solid black"}}]]
            [:label
             {:style {:color (if required? "black" "gray")}}
             "Minimum: "
             [:input
              {:type       :number
               :min        1
               :max        (count foods-in-category)
               :on-focus   #(-> % .-target .select)
               :on-change  (fn [e] (let [num (js/parseInt (.-value (.-target e)))
                                         next-num (if (js/isNaN num) 1 num)]
                                     ;;  (re-frame/dispatch [::events/set-category-min category next-num])
                                     (cond
                                       (> 1 next-num)       ; less than 1
                                       (re-frame/dispatch [::events/set-category-min category 1])
                                       (> next-num maximum) ; greater than maximum
                                       (re-frame/dispatch [::events/set-category-min category maximum])
                                       :otherwise           ; valid number
                                       (re-frame/dispatch [::events/set-category-min category next-num]))))
               :value      @(re-frame/subscribe [::subs/category-min category])
               :style      {:margin-left "0.5em" :border (str "1px solid " (if required? "black" "gray"))
                            :color (if required? "black" "gray")
                            :border-radius :4px}}]]
            [:label "Maximum: "
             [:input
              {:type       :number
               :min        (or @(re-frame/subscribe [::subs/category-min category]) 1)
               :max        (count foods-in-category)
               :on-focus   #(-> % .-target .select)
               :on-change  (fn [e] (let [num (js/parseInt (.-value (.-target e)))
                                         next-num (if (js/isNaN num) 1 num)]
                                     ;; TODO can get into invalid states when used improperly
                                     ;; when user sets maximum below number of already-selected items, it does not
                                     ;; change the order or reset the category
                                     (cond
                                       (> minimum next-num) ; less than minimum
                                       (re-frame/dispatch [::events/set-category-max category minimum])
                                       (< (count foods-in-category) next-num) ; more than possible
                                       (re-frame/dispatch [::events/set-category-max category (count foods-in-category)])
                                       :otherwise ; valid number
                                       (re-frame/dispatch [::events/set-category-max category next-num]))))
               :value      @(re-frame/subscribe [::subs/category-max category])
               :style      {:margin-left "0.5em" :border "1px solid black"
                            :border-radius :4px}}]]
            ])
         [:ul
          (doall
           (for [food foods-in-category]
             ^{:key food}
             [detailed-food-item (:name food) (< num-in-order maximum)]
             ))]
         ]))))

(defn order-selection-menu-2
  []
  (let [offered-foods     @(re-frame/subscribe [::subs/selected-foods])
        kitchen-controls? @(re-frame/subscribe [::subs/show-kitchen-controls?])
        by-category?       (r/atom true)]
    (fn []
      [:div
       [:div
        {:style {:display :flex
                 :justify-content :center
                 :gap     :1em}}
        (when (seq @(re-frame/subscribe [::subs/meal-order]))
          [:button
           {:style {:all :revert}
            :on-click #(re-frame/dispatch [::events/clear-order])}
           "Reset Order"])
        ;; I removed this button. The old mode is barely useful
        #_(when kitchen-controls?
            [:button
             {:style {:all :revert}
              :on-click #(swap! by-category? not)}
             "Change Mode"])
        (when kitchen-controls?
          [:button
           {:style {:all :revert}
            :on-click (fn [_]
                        (re-frame/dispatch [::events/previous-stage])
                        (re-frame/dispatch [::events/toggle-kitchen-controls]))}
           "Ready for Diner"])]
       (if @by-category?
         [:div
          [:h2
           {:style {:text-align :center :font-size "2.5em" :font-weight 700}}
           "Order Selection"]
          (if (seq offered-foods)
            [:div
             [:p
              {:style {:text-align :center :font-size "2em"}}
              "Choose the food you want to eat:"]
             (doall
              (for [category (sort @(re-frame/subscribe [::subs/offered-categories]))]
                ^{:key category}
                [:div
                 [order-category-menu offered-foods category]
                 [:p {:style {:margin-block  :0.5em
                              :text-align   :center
                              :font-size     :1.5em}}
                  "✶"]]))
             [:div
              {:style {:display :flex
                       :justify-content :space-around}}
              [:button
               {:style {:all :revert :font-size "1.5em"}
                :disabled (not (every? (fn [category]
                                         (let [min @(re-frame/subscribe [::subs/category-min category])
                                               max @(re-frame/subscribe [::subs/category-max category])]
                                           (and min max (<= min @(re-frame/subscribe [::subs/num-of-order-items-of-category category])))))
                                       @(re-frame/subscribe [::subs/required-categories])))
                :on-click #(re-frame/dispatch [::events/next-stage])}
               "Continue"]]]
            [:div
             [:p
              {:style {:text-align :center :font-size "1em" :font-weight 600}}
              "No food is currently offered. Please check back later."]
             [:p
              {:style {:text-align :center :font-size "4em"}}
              "⚠️🤷🏻🫙"]])]
         [order-selection-menu])])))

(defn order-confirmation-prompt
  []
  (let [foods @(re-frame/subscribe [::pantry-subs/foods])
        order (sort @(re-frame/subscribe [::subs/meal-order]))
        valid-order? (seq order)]
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
          [:img.food-thumbnail.bigger {:src (-> foods (get food) :img)}]]))
      (when-not valid-order?
        [:p {:style {:text-align :center :font-size "1.5em"
                     :font-weight 700}}
         "You did not choose any food!"])]

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
        :disabled (not valid-order?)
        :on-click #(re-frame/dispatch [::events/next-stage])}
       "Yes"] ;; TODO create record system, which is a simple, vector of timestamped order submissions
      ;;              this opens the door to statistics, which people seem to like for some reason
      [:button
       {:style {:all :revert :font-size "1.5em"}
        :on-click #(re-frame/dispatch [::events/previous-stage])}
       "No"]]]))

(defn preparation-stage
  []
  (let [meal-order @(re-frame/subscribe [::subs/meal-order])]
    [:div
     {:style {:margin-block "4em"}}
     [:p.status-text
      {:style {:text-align :center :font-size "3em"}}
      (if (seq meal-order)
        "🔥"
        "🏖️")]
     [:div.cooking-animation
      [:div.dot]
      [:div.dot]
      [:div.dot]]
     [:p.status-text (if (seq meal-order)
                       "Cooking in progress..."
                       "Kitchen is closed.")]
     [:p {:style {:text-align :center :font-size "0.75em"}}
      (case (count meal-order)
        0 "Gone to lunch."
        1 (str "Preparing your " (first meal-order) ".")
        2 (str "Preparing your " (first meal-order) " and " (second meal-order) ".")
        (apply concat "Preparing your "
               (interpose ", " (take (dec (count meal-order)) meal-order))
               ", and " (last meal-order) ".")
      )]]))

(defn ready-stage
  []
  (let [meal-order @(re-frame/subscribe [::subs/meal-order])]
    [:div {:style {:display :flex :flex-direction :column :align-items :center}}
     [:h1 {:style {:text-align :center
                   :font-size  "6em"}}
      (if (seq meal-order)
        [:div [:span
               {:style {:cursor "pointer"}
                        :on-click #(let [audio (js/Audio. "service-bell.mp3")]
                                     (.play audio))}
               "🛎️"] "🍽️🤵"]
        "🥷💨🍴")]
     [:h2
      {:style {:margin-top    "2em"
               :margin-bottom "1em"
               :font-size     "2em"
               :text-align    :center}}
      "Your food is "
      (if (seq meal-order)
        "ready!"
        "missing!")]
     [:p
      {:style {:margin-bottom "1em"
               :font-size     "1.5em"
               :text-align    :center}}
      (if (seq meal-order)
        "Wash your hands and come to the table."
        "Please remain calm and wait.")
      [:br]
      "Thanks for ordering with us."]
     [:button {:style {:all :revert :font-size "1.5em"}
               :on-click #((re-frame/dispatch [::events/next-stage])
                           (re-frame/dispatch [::events/next-stage])
                           (re-frame/dispatch [::events/clear-order]))}
      "You're welcome. Bye!"]]))

(defn greeting-stage
  []
  (let [custom-greeting @(re-frame/subscribe [::subs/custom-greeting])
        custom-subtitle @(re-frame/subscribe [::subs/custom-subtitle])]
    ;; TODO add open kitchen hours and prevent diner from ordering outside of those hours
    ;;       alternatively, could be on/off switch in control panel
      [:div
       {:style {:margin-block "1em"
                :text-align   :center}}
       (when @(re-frame/subscribe [::subs/show-kitchen-controls?])
         [:div
          ;; TODO better edit triggers
          ;; I'm thinking pencil buttons after each string (visible in parent mode)
          [:label "Custom Greeting: "]
          [:input
           {:style {:border "1px solid black" :border-radius "0.5em" :padding "0.5em"}
            :value custom-greeting
            :on-change #(re-frame/dispatch [::events/set-custom-greeting (-> % .-target .-value)])}]
          [:br]
          [:br]
          [:label "Custom Subtitle: "]
          [:input
           {:style {:border "1px solid black" :border-radius "0.5em" :padding "0.5em"}
            :value custom-subtitle
            :on-change #(re-frame/dispatch [::events/set-custom-subtitle (-> % .-target .-value)])}]])
       ;; TODO add custom emojis too (why not?)
       [:h1 {:style {:text-align :center :font-size :2.5em}}
        (if (seq (.trim custom-greeting))
          custom-greeting
          "Welcome to the Kitchen")]
       [:p  {:style  {:text-align :center :font-size :1.5em}}
        (if (seq (.trim custom-subtitle))
          custom-subtitle
          "We hope you're hungry!")
        ]
       [:h2 {:style {:text-align :center :font-size :4.5em
                     :margin-block :1em}} "🧑🏿‍🍳👨🏻‍🍳👩🏼‍🍳👩🏾‍🍳"]
       [:button {:style {:all :revert :font-size "1.5em"}
                 :on-click #(re-frame/dispatch [::events/next-stage])}
        "Order Food"]]))

(defn main []
  [:div
   {:style {:max-width        :800px
            :margin-block     "1em"
            :margin-inline    :auto
            :padding          "1em"
            :padding-bottom (if (and @(re-frame/subscribe [::subs/show-kitchen-controls?])
                                     ; kind of weird logic, but this is basically only useful on
                                     ;   these particular stages, because they bottom portion
                                     ;   is cut off from view/access
                                     (or (= :order @(re-frame/subscribe [::subs/kitchen-stage]))
                                         (= :offer @(re-frame/subscribe [::subs/kitchen-stage]))))
                              "300px" "1em") ; this is to ensure all the page is visible when
            ;; the control panel is open
            :transition       "padding 0.2s ease-in-out"
            :border           "1px solid black"
            :border-radius    "1em"
            :background-color "white"}}
   [control-panel]
   (case @(re-frame/subscribe [::subs/kitchen-stage])
     :offer
     [food-offering-list (sort-by :name (vals @(re-frame/subscribe [::pantry-subs/foods])))]

     :greet
     [greeting-stage]

     :order
     [order-selection-menu-2]

     :confirm
     [order-confirmation-prompt]

     :prepare
     [preparation-stage]

     :ready
     [ready-stage])])

(comment
  :rcf)