(ns elmer.store
  (:refer-clojure :exclude [get]))

(defprotocol PasteStore
  (get [store name]
    "Get paste.")
  (put [store name key bytes]
    "Persist bytes to store by name, secured with key.")
  (authorized? [store name key]
    "Does this key match stored key for name?"))
