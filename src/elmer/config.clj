(ns elmer.config)

(defn config [& keys]
  (get-in (read-string
           (slurp
            (.getFile
             (first
              (enumeration-seq (-> (Thread/currentThread)
                                   (.getContextClassLoader)
                                   (.getResources "etc/config.clj")))))))
          (vec keys)))
