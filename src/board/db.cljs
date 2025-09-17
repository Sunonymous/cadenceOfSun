(ns board.db)

(def initial-db {:lines     [] ; lines of text to watch
                 :deleted   [] ; once removed, they go here
                 :sections  {} ; groups of lines by title
                 :tomorrow  [] ; lines removed from today and replaced tomorrow
                 :last-used (js/Date.now) ; last time the board was opened
                 :focused-line nil ; line selected to take action upon
                })