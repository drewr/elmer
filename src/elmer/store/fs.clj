(ns elmer.store.fs
  (:use [elmer.store :only [PasteStore]]))

(deftype FsStore [root key-root]
  PasteStore
  (get [_ pname]
    "paste!"))

(defn fs-store [opts]
  (FsStore. (:publish-root opts) (:key-root opts)))
