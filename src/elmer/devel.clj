(ns elmer.devel
  (:require [elmer.core :as elmer]
            [elmer.store fs])
  (:use [compojure.core :only [defroutes GET POST PUT ANY]]
        [ring.adapter.jetty :only [run-jetty]]
        [ring.middleware.file :only [wrap-file]]
        [ring.middleware.file-info :only [wrap-file-info]]
        [ring.middleware.reload :only [wrap-reload]]
        [ring.middleware.stacktrace :only [wrap-stacktrace]]
        [ring.middleware.static :only [wrap-static]]
        [elmer.config :only [config]]
        [elmer.middleware :only [wrap-paste-store]]))

(def app (-> #'elmer/app
             ((fn [app]
                (fn [req]
                  (prn (java.util.Date.))
                  (prn req)
                  (prn (slurp (clojure.java.io/reader (:body req))))
                  (app req))))

             (wrap-paste-store :store (config :store))
             (wrap-file "public")
             (wrap-file-info)
             (wrap-reload '(elmer.core))
             (wrap-stacktrace)))

(defonce server (run-jetty #'app {:port 8085 :join? false}))
