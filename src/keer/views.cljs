(ns keer.views
  (:require
   [keer.subs     :as subs]
   [keer.events   :as events]
   [re-frame.core :as re-frame]
   [keer.util :refer [display-key]]
   [keer.data :refer [interval-names tones-with-flats tones-with-sharps]]))

;; Now I could add a randomized modifier.
;; In the data file, we need a map of modifiers. Each keyword in the map
;; references a separate listing of modifiers. Modifiers are a vector of
;; strings.
;; Thinking about maintaining lists of expressions.
;; Perhaps we were overcomplicating it before.
;; The pattern is:
;; Stored in static data is a set of modifiers.
;; I'm collecting a set of all these items that I've encountered.
;; I can compare them by count to see if I have encountered all of them.
;;; That's a good moment to reset the collection and try again.
;; Otherwise, I can just pluck a random one out of an adjusted collection,
;;; which is the full collection with the seen modifiers removed
;;; and place the new one into the seen collection and the active display

;; That, and we're wanting a better interface for changing the cycle.
;; Need to drag in that metronome from mpt2 and incorporate it into this tool.

;; This is a bit of a philosophical question, almost.
;; Should it be considered a sequential lap if the user has altered the
;; direction or semitone delta of the sequence?
;; One could say it is still A -> A, with a different path...
;; But it's still something distinct to practice, no?
;; To update this to factor in, we would have to encode the summary of the
;; sequence into db. Something like `:0-ascending-5` for <starting-index>-<ascending?>-<semitone-delta>

;; I think there's a discrepancy in the nomenclature here.
;; check index and previous-tone

(defn starting-tone-range []
  (let [use-sharps? @(re-frame/subscribe [::subs/use-sharps?])
        display-keys (map (fn [i] [i (display-key i use-sharps?)]) (range 12))]
    [:div {:style {:width :100%}}
     [:input {:type :range
              :display :block
              :list :starting-tone-values
              :min 0 :max 11
              :value @(re-frame/subscribe [::subs/starting-index])
              :style {:width :100% :margin 0 :padding-block :1rem}
              :on-change #(let [next-starting-index (int (-> % .-target .-value))]
                            (re-frame/dispatch [::events/set-starting-index next-starting-index])
                            (re-frame/dispatch [::events/reset-tone-sequence]))}]
     [:datalist#starting-tone-values
      (doall
       (for [[idx key] display-keys]
         [:option {:key idx :value idx :label key
                   :style {:display :flex
                           :flex-direction :column
                           :justify-content :space-between
                           :width :200px}}]))]]))

(defn tone-delta-range []
  [:div
   [:input
    {:type :range
     :min -11 :max 11
     :step 1
     :style {:-webkit-appearance :slider-vertical
             :position :absolute
             :bottom :50% :right 0
             :transform "translateY(50%)"}
     :disabled  (< (count @(re-frame/subscribe [::subs/tone-sequence])) 12)
     :value     @(re-frame/subscribe [::subs/semitone-adjustment])
     :on-change #(let [next-semitone-adjustment (int (-> % .-target .-value))]
                   (re-frame/dispatch [::events/set-semitone-adjustment next-semitone-adjustment])
                   (re-frame/dispatch [::events/reset-tone-sequence]))}]])

(defn interval-display-text []
  (let [semitone-adjustment @(re-frame/subscribe [::subs/semitone-adjustment])]
    [:span {:style {}}
     [:span (cond
              (pos? semitone-adjustment) "Up a "
              (neg? semitone-adjustment) "Down a "
              :else "")]
     [:span (nth interval-names (abs semitone-adjustment))]]))

;; TODO extract these repeated css styles into class

(defn toggle-sharps-button []
  (let [sharps? @(re-frame/subscribe [::subs/use-sharps?])]
    [:button {:on-click #(re-frame/dispatch [::events/toggle-sharps])
              :style {:font-size :2rem}}
     [:span {:style {:font-weight (when sharps? :bold)
                     :color       (when-not sharps? :gray)}}              "♯"]
     [:span {:style {:font-size :2rem :color :silver}} " / "]
     [:span {:style {:font-weight (when-not sharps? :bold)
                     :color       (when sharps? :gray)}}              "♭"]]))

(defn toggle-ascension []
  (let [semitone-adjustment @(re-frame/subscribe [::subs/semitone-adjustment])]
    [:button {:on-click #(do
                           (re-frame/dispatch
                            [::events/set-semitone-adjustment (* -1 semitone-adjustment)])
                           (re-frame/dispatch [::events/reset-tone-sequence]))
              :style {:font-size :2rem}}
     [:span {:style {:font-weight (when     (pos? semitone-adjustment) :bold)
                     :color       (when-not (pos? semitone-adjustment) :gray)}}              "↑"]
     [:span {:style {:font-size :1.35rem :color :silver}} " / "]
     [:span {:style {:font-weight (when     (neg? semitone-adjustment) :bold)
                     :color       (when-not (neg? semitone-adjustment) :gray)}}              "↓"]]))

(defn toggle-fourths-fifths []
  (let [delta @(re-frame/subscribe [::subs/semitone-adjustment])
        is-fourth?           (= 0 (mod delta 5))]
    [:button {:on-click #(do
                           (re-frame/dispatch
                            [::events/set-semitone-adjustment
                             (cond ;; invert the signal
                               (and is-fourth? (pos? delta)) 7
                               (and is-fourth? (neg? delta)) -7
                               (and (not is-fourth?) (pos? delta)) 5
                               (and (not is-fourth?) (neg? delta)) -5
                               :else :ERROR)])
                           (re-frame/dispatch [::events/reset-tone-sequence]))}
     [:span {:style {:font-weight (when     is-fourth? :bold)
                     :color       (when-not is-fourth? :gray)
                     :font-size   :1.75rem}}              "4th"]
     [:span {:style {:font-size :1.35rem :color :silver}} " / "]
     [:span {:style {:font-weight (when-not is-fourth? :bold)
                     :color       (when     is-fourth? :gray)
                     :font-size   :1.75rem}}              "5th"]]))

(defn controller-1 []
  (let [index    (first @(re-frame/subscribe [::subs/tone-sequence]))
        sharps? @(re-frame/subscribe [::subs/use-sharps?])
        sequenz @(re-frame/subscribe [::subs/tone-sequence])]
    [:div#toneDisplays
     {:style {:margin-block    :2em
              :display         :flex
              :justify-content :center
              :align-items     :center
              :border          "1px solid blue"
              :border-radius   :16px}}
     [:span#previousTone
      {:style {:font-size     :2rem
               :font-family   :monospace
               :color         :lightgray
               :width         :2ch
               :visibility    (if (< 0 (count sequenz) 12) :visible :hidden)
               :margin-inline-end :1em}}
      (display-key @(re-frame/subscribe [::subs/previous-tone]) sharps?)]
     [:div
      {:style {:display         :flex
               :flex-direction  :column
               :align-items     :center
               :justify-content :center
               :flex            1}}
      [:button
       {:style {:font-size       :3rem
                :font-family     :monospace
                :padding         "0.25em 1em"
                :display         :inline-flex
                :justify-content :center
                :align-items     :center
                :whitespace      :nowrap}
        :disabled  (= 0 @(re-frame/subscribe [::subs/semitone-adjustment]))
        :on-click #(re-frame/dispatch [(if (<= (count sequenz) 0)
                                         ::events/reset-tone-sequence
                                         ::events/cycle-tone-sequence)])}
       [:span
        {:style {:display :inline-block
                 :width   :2ch
                 :text-align :center}}
        (or
         (display-key index sharps?)
         "🔁")]]
      ;; ⟲🗘 alternative symbols
      [:br]
      (if (< (count @(re-frame/subscribe [::subs/tone-sequence])) 12)
        [:progress {:max 12 :value (- 12 (count sequenz))}]
        [starting-tone-range])]
     [:span#nextTone
      {:style {:font-size      :2rem
               :text-align     :center
               :font-family    :monospace
               :width          :2ch
               :margin-inline-start  :1em
               :margin-block   :auto}}
      (display-key (second @(re-frame/subscribe [::subs/tone-sequence])) sharps?)]
     ;; [tone-delta-range] ; disabled for now because it became less useful
     ]))

(defn controller-2 []
  (let [index      (first @(re-frame/subscribe [::subs/tone-sequence]))
        sharps?   @(re-frame/subscribe [::subs/use-sharps?])
        sequenz   @(re-frame/subscribe [::subs/tone-sequence])
        prev-tone @(re-frame/subscribe [::subs/previous-tone])
        mid-sequence? (< (count sequenz) 12)]
    [:div {:style {:display        :flex
                   :flex-direction :column
                   :align-items    :center
                   :position       :relative ;; see styling comment in main panel for
                   :transform (if-not mid-sequence? "translateY(2px)" nil)}} ;<-- this
     [:button
      {:style {:display         :flex
               :justify-content :space-between
               :align-items     :center
               :font-size       :3rem
               :font-family     :monospace
               :padding         "0 1em"}
       :on-click #(re-frame/dispatch [(if (<= (count sequenz) 1)
                                        ::events/complete-tone-sequence
                                        ::events/cycle-tone-sequence)])}
      [:span#previousTone
       {:style {:font-size     :2rem
                :font-family   :monospace
                :color         :lightgray
                :width         :2ch
                :visibility    (if prev-tone
                                 :visible :hidden)
                :margin-inline-end :1em}}
       (display-key @(re-frame/subscribe [::subs/previous-tone]) sharps?)]
      [:span
       {:style {:display :inline-block
                :width   :2ch
                :text-align :center}}
       (display-key index sharps?)]
      [:span#nextTone
       {:style {:font-size      :2rem
                :text-align     :center
                :font-family    :monospace
                :color         :lightgray
                :width          :2ch
                :margin-inline-start  :1em
                :margin-block   :auto}}
       (display-key (or
                     (second @(re-frame/subscribe [::subs/tone-sequence]))
                     @(re-frame/subscribe [::subs/starting-index])) sharps?)]]
     (if mid-sequence?
       [:progress {:max 12 :value (- 12 (count sequenz))
                   :style {:width :100%
                           :margin-block "calc(1rem + 3px)"
                           }}]
       [starting-tone-range])]))

(defn laps-display []
  (let [num-laps @(re-frame/subscribe [::subs/number-of-laps])]
    (when (pos? num-laps) ;; this bit of logic should be a level higher,
      [:div.laps-container ;  ie. not within the component itself.
       (for [i (range num-laps)] ;; That would let us convert the state ref
         ^{:key i}                ;  into an argument too.
         [:div {:style {:background-color "#333533"
                        :display :inline-block
                        :margin  :0.25em
                        :width   "1rem"
                        :height  "1rem"
                        :border-radius "50%"}}])])))

(defn main []
  (let [index    (first @(re-frame/subscribe [::subs/tone-sequence]))
        sharps? @(re-frame/subscribe [::subs/use-sharps?])
        sequenz @(re-frame/subscribe [::subs/tone-sequence])
        mid-sequence? (< 0 (count sequenz) 12)]
    [:main
     {:style {:width :100%
              :height :100dvh
              :display :flex
              :flex-direction :column}}
     [:header#statusBar
      {:style {:display :flex
               :justify-content :space-between
               :align-items     :center
               :padding         :1em
               :flex-shrink 1
               :background-color "#333533"
               :color :white}}
      [:div {:style {:font-family :monospace}}
       [:h1 {:style {:margin-bottom :4px}} "keer"]
       [:h3 {:style {:margin-top :4px}} "a tool for practicing all 12 musical keys"]]
      [:div#topControls
       {:style {:width         :fit-content
                :display       :flex
                :gap           :1em}}
       (when mid-sequence?
         [:button
          {:on-click #(re-frame/dispatch [::events/reset-tone-sequence])}
          "Reset"])
       [toggle-sharps-button]
       (when-not mid-sequence?
         [toggle-ascension])
       (when-not mid-sequence?
         [toggle-fourths-fifths])]]
     [:div#mainWindow
      {:style {:flex             2
               :display          :flex
               :flex-direction   :column
               :justify-content  :center
               :background-color "#D6D3C4"
               :align-items      :center}}
      [controller-2]

      ;; The input range to change the tone includes little tick marks
      ;; that cause visual jump when changing to progress. This styling prevents it.
      [:div {:style {:position :relative ;
                     :transform (if-not mid-sequence? "translateY(-2px)" nil)}}
       [laps-display]]]]))
