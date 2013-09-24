(ns elmer.s3
  "** Currently unused! **"
  (:refer-clojure :exclude [get])
  (:import (org.jets3t.service.security AWSCredentials)
           (org.jets3t.service.impl.rest.httpclient RestS3Service)
           (org.jets3t.service.model S3Bucket S3Object)))

(def regions {:us S3Bucket/LOCATION_US
              :us-standard S3Bucket/LOCATION_US_STANDARD
              :us-west S3Bucket/LOCATION_US_WEST
              :europe S3Bucket/LOCATION_EUROPE
              :asia-pacific S3Bucket/LOCATION_ASIA_PACIFIC})

(defn make-service [access-key secret-key]
  (RestS3Service. (AWSCredentials. access-key secret-key)))

(defn make-bucket [svc bucket & {:keys [region]}]
  (let [bucket (.getBucket svc bucket)
        region (regions region)]
    (if bucket
      bucket
      (if region
        (-> svc bucket region)
        (-> svc bucket)))))

(defn make-object [bucket key is len cont]
  (doto (S3Object. bucket key)
    (.setDataInputStream is)
    (.setContentLength len)
    (.setContentType cont)))

(defn put [svc bucket object]
  (.putObject svc bucket object))
