(ns elmer.servlet
  (:use [ring.util.servlet :only [defservice]]
        [elmer.core :only [app]]
        [elmer.context :only [wrap-context-path]])
  (:gen-class
   :extends javax.servlet.http.HttpServlet))

(defservice (wrap-context-path app))
