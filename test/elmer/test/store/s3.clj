(ns elmer.test.store.s3
  (:require [elmer.store :as store]
            [elmer.util :refer [with-tmp-file]])
  (:use [elmer.store.s3] :reload)
  (:use [clojure.test]
        [clojure.java.io :as io]))

(defn creds []
  (-> (str (System/getProperty "user.home")
           "/.elmer.clj")
      slurp read-string :store))

(defn make-paste-name []
  (format "test/elmer-%s.txt" (subs (str (java.util.UUID/randomUUID)) 0 6)))

(deftest ^{:integration true}
  t-paste
  (with-tmp-file [bodyfile "bytes"]
    (let [f (make-paste-name)]
      (testing "should put a paste"
        (with-s3-store [store (creds)]
          (is (store/put store f "sekrat" bodyfile))
          (is (= "bytes" (store/get store f)))))
      (testing "should put a paste from an InputStream"
        (with-s3-store [store (creds)]
          (is (store/put store f "sekrat" bodyfile))
          (is (= "bytes" (store/get store f))))))))

(deftest ^{:integration true}
  t-update
  (with-tmp-file [bodyfile "bytes"]
    (let [f (make-paste-name)]
      (testing "should update a paste, or not if key invalid"
        (with-s3-store [store (creds)]
          (is (store/put store f "sekrat" bodyfile))
          (is (store/put store f "sekrat" bodyfile))
          (is (not (store/put store f "bad" "bytes"))))))))
