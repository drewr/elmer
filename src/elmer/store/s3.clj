(ns elmer.store.s3
  (:require [clojure.tools.logging :as log]
            [aws.sdk.s3 :as s3])
  (:use [elmer.store :only [PasteStore]]))

(defmacro with-s3-store [[store conf] & body]
  `(let [~store (s3-store ~conf)]
     ~@body))

(defn get* [loc pre name]
  (log/debug 'get* name)
  (:content
   (s3/get-object (:creds loc) (:bucket loc) (format "%s/%s" pre name))))

(defn put* [loc pre name keypre key is]
  (log/debug 'put* (:bucket loc) (format "%s/%s" pre name))
  (s3/put-object (:creds loc)
                 (:bucket loc)
                 (format "%s/%s.key" keypre name)
                 key
                 {:content-type "text/plain"})
  (s3/put-object (:creds loc) (:bucket loc) (format "%s/%s" pre name) is))

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

(defn s3-store [{:keys [access-key secret-key
                        bucket key-pastes key-keys]
                 :as opts}]
  (log/debug (format "S3Store (%s):" access-key)
             "bucket" bucket
             "pastes" key-pastes
             "keys" key-keys)
  (S3Store.
   (S3Location. (select-keys opts [:access-key :secret-key]) bucket)
   key-pastes key-keys))
