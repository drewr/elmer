(ns elmer.middleware
  (:require [elmer.store :refer [find-paste-store]]
            [elmer.config :refer [config]]))

(defn wrap-paste-store [handler store]
  (fn [req]
    (handler (assoc req :store store))))
