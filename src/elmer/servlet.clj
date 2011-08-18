(ns elmer.servlet
  (:use [ring.util.servlet :only [defservice]]
        [elmer.core :only [app]]
        [elmer.context :only [wrap-context-path]]
        [elmer.middleware :only [wrap-paste-store]])
  (:gen-class
   :extends javax.servlet.http.HttpServlet))

(defservice (-> app wrap-context-path wrap-paste-store))
