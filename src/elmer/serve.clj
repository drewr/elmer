(ns elmer.serve
  (:require [elmer.core :refer [make-handler]]
            [elmer.context :refer [wrap-context-path]]
            [elmer.config :refer [config-map]]
            [ring.adapter.jetty :as jetty]))

(def embedded
  (make-handler (config-map "elmer-config.clj")))

(def servlet
  (-> embedded
      wrap-context-path))


