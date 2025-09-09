(ns board.db)

(def initial-db {:lines   [] ; lines of text to watch
                 :deleted [] ; once removed, they go here
                 :last-used (js/Date.now) ; last time the board was opened
                })