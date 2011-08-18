(ns elmer.test.store.fs
  (:require [elmer.store :as store])
  (:use [elmer.store.fs] :reload)
  (:use [clojure.test]
        [elmer.util :only [make-dir delete-dir]]))

(defn test-dir []
  (str "tmp/pastes-"
       (.substring
        (str (java.util.UUID/randomUUID)) 0 8)))

(defmacro with-fs-store [store & body]
  (let [dir (test-dir)]
    `(let [~store (fs-store {:publish-root ~dir
                             :key-root ~dir})]
       (make-dir ~dir)
       ~@body
       (delete-dir ~dir))))

(deftest t-paste
  (testing "should put a paste"
    (with-fs-store store
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (= "bytes" (store/get store "foo.txt"))))))

(deftest t-update
  (testing "should update a paste, or not if key invalid"
    (with-fs-store store
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (not (store/put store "foo.txt" "bad" "bytes"))))))
