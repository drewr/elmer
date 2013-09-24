(ns elmer.serve
  (:require [elmer.core :refer [handler]]
            [elmer.context :refer [wrap-context-path]]))

(def embedded handler)

(def servlet (-> handler wrap-context-path))
