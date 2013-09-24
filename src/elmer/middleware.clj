(ns elmer.middleware
  (:require [elmer.store fs s3]
            [elmer.config :refer [config]]))

(defn wrap-paste-store [handler]
  (fn [req]
    (let [store (config :store)
          store ((-> (:factory store) .sym find-var) store)]
      (handler (assoc req :store store)))))
