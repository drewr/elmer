(ns elmer.config
  (:require [elmer.resource :as r]))

(declare config config-resource)

(defn config-map [path]
  (reduce merge {}
          (map (comp r/load #(.getFile %))
               (reverse
                (r/get-resources path)))))

(defn config-resource [& keys]
  (let [path "etc/config.clj"]
    (if-let [val (get-in (config-map path) (vec keys))]
      val
      (throw (Exception. (format "config %s not found in %s" keys path))))))

(def config-strategy config-resource)

(defn config [& keys]
  (apply config-strategy keys))

