(ns elmer.main
  (:gen-class)
  (:require [elmer.core :as elmer]
            [ring.adapter.jetty :as jetty]))

(defn -main [conf]
  (jetty/run-jetty
   (elmer/make-handler (-> conf slurp read-string))
   {:port 8085}))
