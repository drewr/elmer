(ns elmer.io)

(defmacro join-path [& paths]
  `(str (-> ~@(for [p paths] `(java.io.File. ~p)))))

