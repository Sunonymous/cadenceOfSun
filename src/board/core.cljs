(ns board.core (:require
   [reagent.core :as r]
))

;; Let's codify the basics of a virtual working memory.
; Its objects are simple strings of text.
;   IDs? Metadata? Extraneous!
; They are fast to create and effortless to destroy.
;   Such is the nature of the mind.
;
; So there are lines of text.
; In the future, it could be useful to allow inner strings.
; What can we do with these strings?

; [ ] Add a new string
; [ ] Delete an existing string
; [ ] Change a string (which is like creating a string with a shortcut)

(defn main
  []
  [:div#boardWrapper
   "This is the board."
  ])