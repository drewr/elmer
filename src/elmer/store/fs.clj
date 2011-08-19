(ns elmer.store.fs
  (:use [clojure.contrib.duck-streams :only [slurp*]]
        [clojure.java.io :only [file]]
        [elmer.util :only [make-dir delete-dir]]
        [elmer.store :only [PasteStore]]))

(defmacro with-tmp-fs-store [[store dir] & body]
  `(let [dir# ~dir
         ~store (fs-store {:publish-root dir#
                           :key-root dir#})]
     (make-dir dir#)
     ~@body
     (delete-dir dir#)))

(defn get-key [key-root name]
  (-> (format "%s/%s.key" key-root name) file slurp* .trim))

(defn key-exists? [key-root name]
  (-> (format "%s/%s.key" key-root name) file .isFile))

(defn key-valid? [key-root name key]
  (= key (get-key key-root name)))

(defn store-key [key-root name key]
  (let [keyfile (-> (format "%s/%s.key" key-root name)
                    file .getAbsolutePath)]
    (println "INFO" "storing key" keyfile)
    (spit keyfile key)))

(defn getf [root _ name]
  (let [path (format "%s/%s" root name)
        _ (println "INFO loading" (-> path file .getAbsolutePath))]
    (when (.exists (java.io.File. path))
      (slurp path))))

(defn authorized?f [root key-root name key]
  (let [yes? (if (key-exists? key-root name)
               (key-valid? key-root name key)
               true)
        keyfile (-> (format "%s/%s" key-root name) file .getAbsolutePath)]
    (println "INFO authorizing" keyfile "->" yes?)
    yes?))

(defn putf [root key-root name key bytes]
  (when (authorized?f root key-root name key)
    (store-key key-root name key)
    (println "INFO" "storing paste" (-> root file .getAbsolutePath) name)
    (spit (format "%s/%s" root name) bytes)
    true))

(deftype FsStore [root key-root]
  PasteStore
  (get [_ name]
    (getf root key-root name))
  (put [_ name key bytes]
    (putf root key-root name key bytes))
  (authorized? [_ name key]
    (authorized?f root key-root name key)))

(defn fs-store [opts]
  (println "DEBUG ensuring"
           "pastes" (:publish-root opts)
           "keys" (:key-root opts))
  (make-dir (:publish-root opts))
  (make-dir (:key-root opts))
  (FsStore. (:publish-root opts) (:key-root opts)))
