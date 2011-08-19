(ns elmer.servlet
  (:use [ring.util.servlet :only [servlet defservice]]
        [elmer.core :only [app]]
        [elmer.context :only [wrap-context-path]]
        [elmer.middleware :only [wrap-paste-store]]
        [elmer.config :only [config]])
  (:gen-class
   :extends javax.servlet.http.HttpServlet))

(println "DEBUG loading configs"
         (clojure.string/join
          " "
          (elmer.config/get-resources "etc/config.clj")))

(def handler (-> app
                 wrap-context-path
                 (wrap-paste-store :store (config :store))))

(def sl (servlet handler))

(defservice handler)
