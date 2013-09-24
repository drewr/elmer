(defproject elmer "1.1.0"
  :description "Awesome!!!11"
  :exclusions #{org.codehaus.jackson/jackson-core-asl}
  :dependencies [
                 [clj-aws-s3 "0.3.2"]
                 [commons-io "2.4"]
                 [compojure "0.6.4"]
                 [enlive "1.0.0" :exclusions [org.clojure/clojure]]
                 [hiccup "0.3.7"]
                 [log4j/log4j "1.2.16"]
                 [org.antlr/stringtemplate "4.0.2"]
                 [org.clojure/clojure "1.5.0-RC17"]
                 [org.clojure/tools.logging "0.2.3"]
                 [ring/ring "0.3.10"]
                 [org.codehaus.jackson/jackson-core-asl "1.9.12"]]
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler elmer.serve/handler}
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)})
