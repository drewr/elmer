(ns elmer.serve
  (:gen-class)
  (:require [elmer.core :refer [make-handler]]
            [elmer.store :refer [find-paste-store]]
            ;; [elmer.context :refer [wrap-context-path]]
            ;; [elmer.config :refer [config-map]]
            [ring.adapter.jetty :as jetty]))

;; Can be supplied to :ring {:handler ...}
;; (def embedded
;;   (make-handler (config-map "elmer-config.clj")))
;;
;; Can be supplied to :ring {:servlet-class-name ...}
;; (def servlet
;;   (-> embedded
;;       wrap-context-path))

(defn main [conf join?]
  (if-let [store (find-paste-store (:store conf))]
    (jetty/run-jetty
     (make-handler
      (assoc conf :store store))
     {:port (:port conf 8085)
      :join? join?})
    (throw
     (Exception.
      (with-out-str
        (print "no store found for conf")
        (prn conf))))))

(defn -main [& args]
  (main (-> (first args) slurp read-string) true))
