(ns elmer.servlet
  (:use [compojure.core]
        [ring.util.servlet :only [defservice]]
        [elmer.core :only [serve-paste post-paste info-paste
                              home not-found]])
  (:gen-class
   :extends javax.servlet.http.HttpServlet))

(defroutes go
  (GET "/paste/:paste.:ext" [paste ext]
       (serve-paste (format "%s.%s" paste ext)))
  (GET "/paste/sh" request
       (info-paste request))
  (GET "/paste/" request
       (home request))
  (POST "/paste/" request
        (post-paste request))
  (ANY "*" request
       {:status 404, :body (not-found request)}))

(defservice go)
