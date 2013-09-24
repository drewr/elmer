(ns elmer.util
  (:require [clojure.java.io :as io])
  (:import (java.io File)))

(defn test-dir []
  (str "tmp/pastes-"
       (.substring
        (str (java.util.UUID/randomUUID)) 0 8)))

(defn make-dir [dir]
  (.mkdirs (io/file dir)))

(defn delete-dir [dir]
  (let [f (io/file dir)]
    (if (.isDirectory f)
      (do
        (doseq [g (reverse (file-seq f))]
          (.delete g))
        true)
      false)))

(defmacro with-tmp-file [[sym contents] & body]
  `(let [~sym (File/createTempFile "elmer-" ".txt")]
     (try
       (io/copy ~contents (io/file ~sym))
       ~@body
       (finally
         (.delete ~sym)))))
