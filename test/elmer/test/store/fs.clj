(ns elmer.test.store.fs
  (:require [elmer.store :as store])
  (:use [elmer.store.fs] :reload)
  (:use [clojure.test]
        [elmer.util :only [test-dir]]))

(deftest t-paste
  (testing "should put a paste"
    (with-tmp-fs-store [store (test-dir)]
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (= "bytes" (store/get store "foo.txt")))))
  (testing "should put a paste from an InputStream"
    (with-tmp-fs-store [store (test-dir)]
      (is (store/put store "foo.txt" "sekrat" (java.io.ByteArrayInputStream.
                                               (.getBytes "bytes"))))
      (is (= "bytes" (store/get store "foo.txt"))))))

(deftest t-update
  (testing "should update a paste, or not if key invalid"
    (with-tmp-fs-store [store (test-dir)]
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (not (store/put store "foo.txt" "bad" "bytes"))))))
