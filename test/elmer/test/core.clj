(ns elmer.test.core
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [elmer.store.fs :refer [with-tmp-fs-store]]
            [elmer.util :refer [test-dir]])
  (:require [elmer.core :refer :all]
            :reload))

(deftest t-post
  (with-tmp-fs-store [store (test-dir)]
    (let [body (fn [] (io/input-stream (.getBytes "bytes" "utf-8")))
          key (atom nil)
          resp (post-paste {:uri "/foo.txt"
                            :store store
                            :body (body)})]
      (testing "post paste"
        (is (= 200 (:status resp)))
        (reset! key (get-in resp [:headers "X-Key"])))
      (testing "repost without key"
        (is (= 401 (:status (post-paste {:uri "/foo.txt"
                                         :store store
                                         :body (body)})))))
      (testing "repost with key"
        (is (= 200 (:status (post-paste {:uri "/foo.txt"
                                         :headers {"x-key" @key}
                                         :store store
                                         :body (body)})))))
      (testing "get content"
        (is (= "bytes" (:body (serve-paste store "/foo.txt"))))))))
