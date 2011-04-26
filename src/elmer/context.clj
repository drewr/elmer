(ns elmer.context)

(defn wrap-context-path [app]
  (fn [req]
    (let [path (-> req :servlet-request .getContextPath)
          req (assoc req :context-path path)]
      (if (:context-path req)
        (app (assoc req
               :uri (.replaceFirst (:uri req) (:context-path req) "")))
        (app req)))))

