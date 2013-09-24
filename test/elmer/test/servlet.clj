(ns elmer.test.servlet
  (:require [clojure.java.io :as io])
  (:use [elmer.serve] :reload)
  (:use [clojure.test]
        [elmer.util :only [test-dir]]
        [elmer.core :only [post-paste serve-paste]]
        [elmer.store.fs :only [with-tmp-fs-store]]))

(def store (atom nil))

(use-fixtures
 :once
 (fn [t]
   (with-tmp-fs-store [_store (test-dir)]
     (let [body (io/input-stream (.getBytes "bytes" "utf-8"))
           key (atom nil)
           resp (post-paste {:uri "/foo.txt"
                             :store _store
                             :body body})]
       (reset! key (get-in resp [:headers "x-key"]))
       (reset! store _store)
       (t)))))

(deftest t-get
  (testing "*** get"
    (is (= "bytes" (:body (serve-paste @store "/foo.txt"))))
    ;; Figure out how to send a doGet to proxied HttpServlet
    #_(is (= :foo (doto sl clojure.contrib.repl-utils/show)))))

