(ns elmer.middleware
  (:require [elmer.store fs s3]
            [elmer.config :refer [config]]))

(defn wrap-paste-store [handler config]
  (if-let [store (config :store)]
    (let [store ((-> (:factory store) .sym find-var) store)]
      (fn [req]
        (handler (assoc req :store store))))
    (throw (Exception. "no :store found in config"))))
