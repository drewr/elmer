(ns elmer.store
  (:refer-clojure :exclude [get]))

(defprotocol PasteStore
  (get [store name]
    "Get paste.")
  (put [store key name bytes]
    "Persist bytes to store by name, secured with key.")
  (authorized? [store key name]
    "Does this key match stored key for name?"))
