(ns elmer.middleware)

(defn wrap-paste-store [handler & {:keys [store]}]
  (let [store ((-> store :type second resolve) store)]
    (fn [req]
      (handler (assoc req :store store)))))

