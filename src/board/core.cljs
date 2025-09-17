(ns board.core (:require
                [board.subs    :as subs]
                [board.events  :as events]
                [re-frame.core :as re-frame]))

;; Let's codify the basics of a virtual working memory.
; Its objects are simple strings of text.
;   IDs? Metadata? Extraneous!
; They are fast to create and effortless to destroy.
;   Such is the nature of the mind.
;
; So there are lines of text.
; In the future, it could be useful to allow inner strings.
; What can we do with these strings?

(defn add-line-button
  [section-title]
  [:button
   {:style {:all       :revert
            :font-size :1.5rem}
    :on-click #(let [new-line (js/prompt "What's on your mind?")]
                 (when (seq (.trim new-line))
                   (if section-title
                     (re-frame/dispatch [::events/add-line-to-section section-title new-line])
                     (re-frame/dispatch [::events/add-line new-line]))))}
   "➕"])

(defn add-section-button
  []
  [:button
   {:style {:all       :revert
            :font-size :1.5rem
            :margin-left :8px}
    :on-click #(let [title (js/prompt "Group title:")]
                 (when (seq (.trim title))
                   (re-frame/dispatch [::events/add-section title])))}
   "[ + ]"])

(defn move-to-tomorrow-button
  []
  [:button
   {:style {:all       :revert
            :font-size :1.5rem
            :margin-left :8px}
    :on-click #(re-frame/dispatch [::events/move-to-tomorrow @(re-frame/subscribe [::subs/focused-line])])}
   "🔜"])

(defn back-button
  []
  [:button
   {:style {:all :revert
            :font-size :1.5rem
            :margin-bottom :16px}
    :on-click #(re-frame/dispatch [::events/deactivate-section])}
   "← Back"])

(defn board-reset-warning
  []
  (let [today    (js/Date. @(re-frame/subscribe [::subs/last-used]))
        tomorrow (js/Date. (.getFullYear today) (.getMonth today) (inc (.getDate today)))]
    [:span#resetWarning
     {}
     "board resets on " (.toLocaleDateString tomorrow)]))

(defn line-component
  [line section-title]
  (let [is-focused? (= line @(re-frame/subscribe [::subs/focused-line]))]
    [:article
     {:class (if is-focused? "animated-gradient" "") ; animation is in pantry_styles.css
      :on-click #(re-frame/dispatch [::events/toggle-line-focus line])
      :style {:padding         "0.5rem"
              :display         :flex
              :justify-content :space-between
              :align-items     :center
              :margin-left     (if section-title "1rem" "0")
              :font-style      (if is-focused? "oblique 22deg" "normal")
              :user-select     :none
             }}
     [:span
      {:style {:font-family "Playfair Display"
               :font-size   :1.2rem}}
      line]
     [:button
      {:style {:all       :revert
               :font-size :1.5rem}
       :on-click #(if section-title
                    (re-frame/dispatch [::events/delete-line-from-section section-title line])
                    (re-frame/dispatch [::events/delete-line line]))}
      "×"]]))

(defn section-header
  [section-title]
  [:div
   {:style {:display :flex
            :justify-content :space-between
            :align-items :center
            :cursor :pointer
            :padding "0.5rem"
            :margin "0.5rem 0"}
    :on-click #(re-frame/dispatch [::events/activate-section section-title])}
   [:h3
    {:style {:font-family "Playfair Display"
             :font-size :1.3rem
             :margin 0
             :color "#374151"}}
    (str "[" section-title "]")]
   [:button
    {:style {:all :revert
             :font-size :1.2rem}
     :on-click #(do (.stopPropagation %)
                    (re-frame/dispatch [::events/delete-section section-title]))}
    "×"]])

(defn section-view
  [section-title section-lines]
  [:div
   [back-button]
   [:h2
    {:style {:font-family "Playfair Display"
             :font-size :1.8rem
             :margin "0 0 1rem 0"
             :color "#374151"}}
    (str "[" section-title "]")]
   [:div
    {:style {:margin "1rem 0"}}
    [add-line-button section-title]]
   [:div
    {:style {:display        :flex
             :flex-direction :column
             :gap            :8px}}
    (doall
     (for [line section-lines]
       ^{:key (str section-title "-" line)}
       [line-component line section-title]))]])



(defn main
  []
  (let [active-section @(re-frame/subscribe [::subs/active-section])
        sections       @(re-frame/subscribe [::subs/sections])
        lines          @(re-frame/subscribe [::subs/lines])]
    [:div#boardWrapper
     {:style {:max-width       :500px
              :margin-inline   :auto
              :display         :flex
              :flex-direction  :column
              :justify-content :center}}
     [board-reset-warning]

     (if active-section
       ;; Section view - show only the active section
       [section-view active-section @(re-frame/subscribe [::subs/lines-in-section active-section])]

       ;; Main view - show all content and controls
       [:div
        [:div#controlsWrapper
         {:style {:display :flex
                  :gap     :8px
                  :margin  "1rem 0"}}
         [add-line-button nil]
         [add-section-button]
         (when @(re-frame/subscribe [::subs/focused-line])
           [move-to-tomorrow-button])]
        [:div#contentWrapper
         {:style {:display         :flex
                  :flex-direction  :column
                  :gap             :8px}}
         ;; Regular lines
         (doall
          (for [line lines]
            ^{:key line}
            [line-component line nil]))
         ;; Section headers
         (doall
          (for [[section _] sections]
            ^{:key section}
            [section-header section]))]])]))

(comment)