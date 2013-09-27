(ns elmer.template
  (:require [elmer.resource :as resource]
            [elmer.util :refer [join-path]])
  (:import (org.stringtemplate.v4 ST)))

(defn make-template [s vs]
  (let [st (ST. s)]
    (doseq [[k v] vs]
      (.add st (name k) v))
    st))

(defn load* [path ctx]
  (make-template (resource/load path) ctx))

(defn render [path ctx]
  (.render (load* path ctx)))

(defn render-template [root tmpl ctx]
  (render (join-path root tmpl) ctx))
