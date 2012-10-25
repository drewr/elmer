(defproject elmer "1.0.0"
  :description "Awesome!!!11"
  :repositories {"scala-tools" "http://scala-tools.org/repo-releases/"}
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/tools.logging "0.2.3"]
                 [log4j/log4j "1.2.16"]
                 [enlive "1.0.0-SNAPSHOT"]
                 [compojure "0.6.4"]
                 [hiccup "0.3.7"]
                 [ring/ring "0.3.5"]
                 [org.markdownj/markdownj "0.3.0-1.0.2b4"]
                 [org.antlr/stringtemplate "4.0.2"]
                 [clj-aws-s3 "0.3.2"]]
  :dev-dependencies [[swank-clojure "1.3.1"]
                     [lein-release "1.1.1"]
                     [uk.org.alienscience/leiningen-war "0.0.13"]]
  :aot [elmer.servlet]
  :war {:web-content "public"})
