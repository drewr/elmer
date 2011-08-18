(ns elmer.test.store.fs
  (:require [elmer.store :as store])
  (:use [elmer.store.fs] :reload)
  (:use [clojure.test]))

(defn test-dir []
  (str "tmp/pastes-"
       (.substring
        (str (java.util.UUID/randomUUID)) 0 8)))

(deftest t-paste
  (testing "should put a paste"
    (with-tmp-fs-store [store (test-dir)]
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (= "bytes" (store/get store "foo.txt"))))))

(deftest t-update
  (testing "should update a paste, or not if key invalid"
    (with-tmp-fs-store [store (test-dir)]
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (not (store/put store "foo.txt" "bad" "bytes"))))))
