(ns elmer.run
  (:use [clojure.contrib.duck-streams :only [slurp*]]
        [compojure.core]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [elmer.config]))

(defn get-time []
  (-> (java.util.Date.) .getTime))

(defn serve-paste [filename]
  (let [path (format "%s/%s" (-> (config :publish-root)
                                 java.io.File.
                                 .getAbsolutePath)
                     filename)]
    (if (.exists (java.io.File. path))
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body (slurp path)}
      {:status 404
       :body (format "%s not found" filename)})))

(defn post-paste [{:keys [uri body] :as request}]
  (let [file (format "%s.txt" (get-time))
        path (format "%s/%s" (config :publish-root) file)
        paste-url (format "%s/%s" (config :public-url) file)]
    (spit path (slurp* body))
    paste-url))

(defroutes go
  (GET "/:paste.:ext" [paste ext]
       (serve-paste (format "%s.%s" paste ext)))
  (POST "/" request
        (post-paste request))
  (ANY "*" []
       {:status 404, :body "<h1>Page not found</h1>"}))

(def app (-> #'go (wrap-reload '(elmer.run)) (wrap-stacktrace)))

(defonce *app* (run-jetty #'app {:port 8085 :join? false}))

