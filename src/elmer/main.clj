(ns elmer.main
  (:gen-class)
  (:require [elmer.core :as elmer]
            [ring.adapter.jetty :as jetty]))

(defn -main [& args]
  (prn args)
  (jetty/run-jetty
   (elmer/make-handler (-> (first args) slurp read-string))
   {:port 8085}))
