(defproject elmer "1.2-SNAPSHOT"
  :description "Awesome!!!11"
  :exclusions #{org.codehaus.jackson/jackson-core-asl}
  :dependencies [[clj-aws-s3 "0.3.2"]
                 [commons-io "2.4"]
                 [compojure "1.1.5" :exclusions [ring/ring-core]]
                 [enlive "1.1.1" :exclusions [org.clojure/clojure]]
                 [hiccup "1.0.4"]
                 [log4j/log4j "1.2.16"]
                 [org.antlr/stringtemplate "4.0.2"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.logging "0.2.3"]
                 [ring/ring "1.2.0"]
                 [org.codehaus.jackson/jackson-core-asl "1.9.12"]]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.2.0"]]}}
  :plugins [[lein-ring "0.8.7"]]
  :main elmer.serve
  :aot [elmer.serve]
  ;; :ring {:handler elmer.serve/embedded
  ;;        :servlet-class-name elmer.serve/servlet}
  :test-selectors {:default (complement :integration)
                   :integration :integration
                   :all (constantly true)}
  :jar-name "elmer-%s-nodeps.jar"
  :uberjar-name "elmer-%s.jar")
