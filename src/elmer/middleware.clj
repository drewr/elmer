(ns elmer.middleware
  (:require [elmer.store fs s3]
            [elmer.config :refer [config]]))

(defn wrap-paste-store [handler]
  (fn [req]
    (if-let [storecfg (-> req :elmer :store)]
      (let [store ((-> (:factory storecfg) .sym find-var) storecfg)]
        (handler (assoc req :store store)))
      (throw (Exception. "no :store found in config")))))
