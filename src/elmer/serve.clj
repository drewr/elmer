(ns elmer.serve
  (:require [elmer.core :refer [make-handler]]
            [elmer.context :refer [wrap-context-path]]
            [elmer.config :refer [config-map]]))

(def embedded
  (make-handler (config-map "elmer-config.clj")))

(def servlet
  (-> (make-handler (config-map "elmer-config.clj"))
      wrap-context-path))
