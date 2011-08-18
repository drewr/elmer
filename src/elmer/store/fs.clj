(ns elmer.store.fs
  (:use [clojure.contrib.duck-streams :only [slurp*]]
        [clojure.java.io :only [file]]
        [elmer.store :only [PasteStore]]))

(defn get-key [key-root name]
  (-> (format "%s/%s.key" key-root name) file slurp* .trim))

(defn key-exists? [key-root name]
  (-> (format "%s/%s.key" key-root name) file .isFile))

(defn key-valid? [key-root name key]
  (= key (get-key key-root name)))

(defn store-key [key-root name key]
  (spit (format "%s/%s.key" key-root name) key))

(defn getf [root _ name]
  (let [path (format "%s/%s" root name)]
    (when (.exists (java.io.File. path))
      (slurp path))))

(defn authorized?f [root key-root name key]
  (if (key-exists? key-root name)
    (key-valid? key-root name key)
    true))

(defn putf [root key-root name key bytes]
  (when (authorized?f root key-root name key)
    (store-key key-root name key)
    (spit (format "%s/%s" root name) bytes)
    true))

(deftype FsStore [root key-root]
  PasteStore
  (get [_ name]
    (getf root key-root name))
  (put [_ name key bytes]
    (putf root key-root name key bytes))
  (authorized? [_ name key]
    (authorized?f root key-root name key)))

(defn fs-store [opts]
  (FsStore. (:publish-root opts) (:key-root opts)))
