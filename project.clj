(defproject elmer "1.1.0"
  :description "Awesome!!!11"
  :dependencies [[org.clojure/clojure "1.5.0-RC17"]
                 [org.clojure/tools.logging "0.2.3"]
                 [log4j/log4j "1.2.16"]
                 [enlive "1.0.0"]
                 [compojure "0.6.4"]
                 [hiccup "0.3.7"]
                 [ring/ring "0.3.10"]
                 [org.markdownj/markdownj "0.3.0-1.0.2b4"]
                 [org.antlr/stringtemplate "4.0.2"]
                 [clj-aws-s3 "0.3.2"]
                 [commons-io "2.4"]]
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler elmer.servlet/handler})
