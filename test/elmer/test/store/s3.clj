(ns elmer.test.store.s3
  (:require [elmer.store :as store])
  (:use [elmer.store.s3] :reload)
  (:use [clojure.test]
        [clojure.java.io :as io]))

(deftest t-paste
  (testing "should put a paste"
    (with-s3-store [store (-> (str (System/getProperty "user.home")
                                   "/.elmer.clj")
                              slurp read-string)]
      (is (store/put store "foo.txt" "sekrat" "bytes"))
      (is (= "bytes" (slurp (store/get store "foo.txt"))))))
  )
