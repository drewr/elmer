(ns elmer.test.core
  (:use [elmer.store.fs :only [with-tmp-fs-store]])
  (:use [elmer.core] :reload)
  (:use [clojure.test]
        [elmer.util :only [test-dir]]))

(deftest t-post
  (with-tmp-fs-store [store (test-dir)]
    (let [body (fn [] (java.io.StringReader. "bytes"))
          key (atom nil)
          resp (post-paste {:uri "/foo.txt"
                            :store store
                            :body (body)})]
      (testing "post paste"
        (is (= 200 (:status resp)))
        (reset! key (get-in resp [:headers "x-key"])))
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
