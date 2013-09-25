(ns elmer.main
  (:gen-class)
  (:require [elmer.core :as elmer]
            [elmer.config :refer [config-map]]
            [ring.adapter.jetty :as jetty]))

(defn -main [config]
  (jetty/run-jetty
   (elmer/make-handler (config-map config))
   {:port 8085}))
