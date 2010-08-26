(ns elmer.devel
  (:use [compojure.core :only [defroutes GET POST ANY]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [elmer.core :only [serve-paste post-paste info-paste]]))

(defroutes go
  (GET "/:paste.:ext" [paste ext]
       (serve-paste (format "%s.%s" paste ext)))
  (GET "/sh" request
        (info-paste request))
  (POST "/" request
        (post-paste request))
  (ANY "*" []
       {:status 404, :body "<h1>Page not found</h1>"}))

(def app (-> #'go (wrap-reload '(elmer.run)) (wrap-stacktrace)))

(defonce *app* (run-jetty #'app {:port 8085 :join? false}))

