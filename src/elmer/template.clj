(ns elmer.template
  (:use [elmer.config]
        [elmer.io :only [join-path]])
  (:import (org.stringtemplate.v4 ST)))

(defn make-template [s vs]
  (let [st (ST. s)]
    (doseq [[k v] vs]
      (.add st (name k) v))
    st))

(defn load* [path ctx]
  (make-template
   (slurp (get-resource-file path)) ctx))

(defn render [path ctx]
  (.render (load* path ctx)))

(defn render-template [tmpl ctx]
  (render (join-path (config :template-root) tmpl) ctx))
