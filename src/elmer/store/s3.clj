(ns elmer.store.s3
  (:require [clojure.tools.logging :as log])
  (:use [elmer.store :only [PasteStore]]))

(defn get* []
  )

(defn put* []
  )

(defn authorized?* []
  )



(deftype S3Store [loc pastes keys]
  PasteStore
  (get [_ name]
    (get*))
  (put [_ name key is]
    (put*))
  (authorized? [_ name key]
    (authorized?*)))

(defrecord S3Location [acc key bucket])

(defn s3-store [{:keys [accesskey secret
                        bucket key-pastes key-keys]
                 :as opts}]
  (log/debug (format "S3Store (%s):" accesskey)
             "bucket" bucket
             "pastes" key-pastes
             "keys" key-keys)
  (S3Store. accesskey secret bucket key-pastes key-keys))

