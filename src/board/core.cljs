(ns board.core (:require
                [reagent.core  :as r]
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

; [x] Add a new string
; [ ] Delete an existing string
; [ ] Change a string (which is like creating a string with a shortcut)

(defn front-add-button
  []
  [:button#addNewLine
   {:style {:all       :revert
            :font-size :2rem}
    :on-click #(let [new-line (js/prompt "What's on your mind?")]
                 (when (seq (.trim new-line))
                   (re-frame/dispatch [::events/add-line new-line])))}
   "➕"])

(defn main
  []
  [:div#boardWrapper
   {:style {:max-width       :500px
            :margin-inline   :auto
            :display         :flex
            :flex-direction  :column
            :justify-content :center}}
   [:div#linesWrapper
    {:style {:display         :flex
             :flex-direction  :column
             :justify-content :center
             :gap             :16px
            }}
    [front-add-button]
    (doall
     (for [line @(re-frame/subscribe [::subs/lines])]
       ^{:key line} ; not necessarily unique, but that's up to the user
       [:article#line
        {:style {:padding         "1rem 0.5rem"
                ;;  :border          "1px solid magenta"
                 :display         :flex
                 :justify-content :space-between
                 :align-items     :center}}
        [:h2
         {:style {:font-family "Playfair Display"
                  :font-size   :1.5rem}}
         line]
        [:button
         {:style {:all       :revert
                  :font-size :2rem}
          :on-click #(re-frame/dispatch [::events/delete-line line])}
         "➡️"]]))
   ]])

(comment

  )