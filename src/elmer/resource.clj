(ns elmer.resource
  (:refer-clojure :exclude [load]))

(defn get-resource [path]
  (.getResource
   (ClassLoader/getSystemClassLoader) path))

(defn get-resources [path]
  (enumeration-seq
   (.getResources
    (ClassLoader/getSystemClassLoader) path)))

(defn get-resource-file [path]
  (.getFile (get-resource path)))

(defn load [path]
  (-> path get-resource slurp))

(defn load-clj [path]
  (-> path load read-string))
