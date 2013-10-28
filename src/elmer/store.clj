(ns elmer.store
  (:require [elmer.store fs s3]))

(defn find-paste-store [cfg]
  ((-> (:factory cfg) .sym find-var) cfg))
