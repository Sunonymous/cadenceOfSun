(ns cos.events
  (:require
   [re-frame.core :as re-frame]
   [tools.util :refer [inc-wrap-index]]
   [cos.db :as db]
   [keer.db :as keer-db]
   ))

(re-frame/reg-event-db ::initialize-db  (constantly (merge keer-db/default-db
                                                           db/default-db)))

