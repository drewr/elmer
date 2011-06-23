(ns elmer.config)

(declare config config-resource)

(defn get-resource [path]
  (.getResource
   (.getClassLoader (class config)) path))

(defn get-resources [path]
  (enumeration-seq
   (.getResources
    (.getClassLoader (class config)) path)))

(defn get-resource-file [path]
  (.getFile (get-resource path)))

(defn load-resource [path]
  (read-string (slurp path)))

(defn config-map [path]
  (reduce merge {}
          (map (comp load-resource
                     #(.getFile %))
               (reverse
                (get-resources path)))))

(defn config-resource [& keys]
  (let [path "etc/config.clj"]
    (if-let [val (get-in (config-map path) (vec keys))]
      val
      (throw (Exception. (format "config %s not found in %s" keys path))))))

(def config-strategy config-resource)

(defn config [& keys]
  (apply config-strategy keys))

