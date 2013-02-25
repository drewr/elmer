(ns elmer.servlet
  (:require [clojure.tools.logging :as log]
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
                 wrap-context-path
                 (wrap-paste-store :store (config :store))))
