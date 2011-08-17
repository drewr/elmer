(ns elmer.store
  (:refer-clojure :exclude [get]))

(defprotocol PasteStore
  (get [store pname]
    "Get paste.")
  (put [store key pname bytes]
    "Persist bytes to store by name, secured with key.")
  (authorized? [store key pname]
    "Does this key match stored key for pname?"))
