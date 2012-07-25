(ns elmer.store.s3
  (:require [clojure.tools.logging :as log]
            [aws.sdk.s3 :as s3])
  (:use [elmer.store :only [PasteStore]]))

(defn get* [loc pre name]
  (log/debug 'get* name)
  (:content
   (s3/get-object (:creds loc) (:bucket loc) (format "%s%s" pre name))))

(defn put* []
  )

(defn authorized?* []
  )

(deftype S3Store [loc pastes keys]
  PasteStore
  (get [_ name]
    (get* loc pastes name))
  (put [_ name key is]
    (put* loc pastes name keys key is))
  (authorized? [_ name key]
    (authorized?*)))

(defrecord S3Location [creds bucket])

(defn s3-store [{:keys [s3key s3secret
                        bucket key-pastes key-keys]
                 :as opts}]
  (log/debug (format "S3Store (%s):" s3key)
             "bucket" bucket
             "pastes" key-pastes
             "keys" key-keys)
  (S3Store.
   (S3Location. {:access-key s3key :secret-key s3secret} bucket)
   key-pastes key-keys))
