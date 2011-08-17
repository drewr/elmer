(ns elmer.store.fs
  (:use [clojure.contrib.duck-streams :only [slurp*]]
        [elmer.store :only [PasteStore]]))

(defn store-key [key-root name key]
  (spit (format "%s/%s.key" key-root name) key))

(defn getf [root _ pname]
  (let [path (format "%s/%s" root pname)]
    (when (.exists (java.io.File. path))
      (slurp path))))

(defn authorized?f [root key-root key pname]
  (try
    (= key (slurp* (format "%s/%s.key" key-root pname)))
    (catch java.io.FileNotFoundException _ false)))

(defn putf [root key-root key pname bytes]
  (when (not (authorized?f root key-root key pname))
    (store-key key-root pname key))
  (spit (format "%s/%s" root pname) bytes))

(deftype FsStore [root key-root]
  PasteStore
  (get [_ name]
    (getf root key-root name))
  (put [_ key name bytes]
    (putf root key-root key name bytes))
  (authorized? [_ key name]
    (authorized?f root key-root key name)))

(defn fs-store [opts]
  (FsStore. (:publish-root opts) (:key-root opts)))
