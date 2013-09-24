(ns elmer.serve
  (:require [clojure.tools.logging :as log]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [elmer.store fs s3]
            [elmer.core :refer [app]]
            [elmer.context :refer [wrap-context-path]]
            [elmer.middleware :refer [wrap-paste-store]]
            [elmer.config :refer [config]]))

(log/debug "config"
           (clojure.string/join
            " "
            (elmer.config/get-resources "etc/config.clj")))

(def handler (-> app
                 (wrap-paste-store :store (config :store))
                 (wrap-resource "public")
                 wrap-content-type
                 wrap-context-path))
