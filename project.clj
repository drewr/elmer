(defproject elmer "1.0.0-SNAPSHOT"
  :description "Awesome!!!11"
  :repositories {"scala-tools" "http://scala-tools.org/repo-releases/"}
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [enlive "1.0.0-SNAPSHOT"]
                 [compojure "0.4.1"]
                 [hiccup "0.2.6"]
                 [ring/ring "0.3.5"]
                 [org.markdownj/markdownj "0.3.0-1.0.2b4"]]
  :dev-dependencies [[swank-clojure "1.2.1"]
                     [lein-release "1.1.1"]
                     [uk.org.alienscience/leiningen-war "0.0.13"]]
  :namespaces [elmer.servlet]
  :war {:name "elmer.war"
        :web-content "public"})
