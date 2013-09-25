(ns elmer.resource)

(declare foo)

(defn get-resource [path]
  (.getResource
   (.getClassLoader (class foo)) path))

(defn get-resources [path]
  (enumeration-seq
   (.getResources
    (.getClassLoader (class foo)) path)))

(defn get-resource-file [path]
  (.getFile (get-resource path)))

(defn load-resource [path]
  (read-string (slurp path)))

