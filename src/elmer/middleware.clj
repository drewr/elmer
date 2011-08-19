(ns elmer.middleware
  (:require [elmer.store fs]))

(defn wrap-paste-store [handler & {:keys [store]}]
  (let [store ((-> (:factory store) .sym find-var) store)]
    (fn [req]
      (handler (assoc req :store store)))))

