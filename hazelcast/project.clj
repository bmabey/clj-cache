(defproject clj-cache-hazelcast "0.0.4"
  :description "Hazelcast support for clj-cache"
  :dependencies [[com.hazelcast/hazelcast "1.9.3"]
                 [clj-cache "0.0.4"]]
  :dev-dependencies [[org.clojure/clojure "1.2.0"]
                     [org.clojure/clojure-contrib "1.2.0"]
                     [swank-clojure "1.2.1"]
                     [log4j/log4j "1.2.13"]]
  :warn-on-reflection true
  :jar-exclusions [#"log4j\.properties"]
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"})

