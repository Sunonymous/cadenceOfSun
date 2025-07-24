(ns cos.events
  (:require
   [re-frame.core :as re-frame]
   [tools.util :refer [inc-wrap-index]]
   [cos.db :as db]
   [keer.db :as keer-db]
   [kitchen.db :as kitchen-db]
   ))

(re-frame/reg-event-db ::initialize-db  (constantly (merge keer-db/default-db    ;; Add DB for Keer Tool
                                                           kitchen-db/default-db ;; Add DB Kitchen Tool
                                                           db/default-db)))      ;; Home DB

