(ns elmer.store.s3
  (:require [clojure.tools.logging :as log])
  (:use [elmer.store :only [PasteStore]]))

(defn get* []
  )

(defn put* []
  )

(defn authorized?* []
  )



(deftype S3Store [root key-root]
  PasteStore
  (get [_ name]
    (get* root key-root name))
  (put [_ name key bytes]
    (put* root key-root name key bytes))
  (authorized? [_ name key]
    (authorized?* root key-root name key)))

(defn s3-store [opts]
  (log/debug "pastes" (:publish-root opts)
             "keys" (:key-root opts))
  (S3Store. (:publish-root opts) (:key-root opts)))
