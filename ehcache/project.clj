(defproject clj-cache-ehcache "0.0.5"
  :description "Ehcache support for clj-cache"
  :dependences [[net.sf.ehcache/ehcache-core "2.4.7"]
                [clj-cache "0.0.4"]]
  :multi-deps {"1.4" [[org.clojure/clojure "1.4.0"]]
               "1.3" [[org.clojure/clojure "1.3.0"]]
               "1.2" [[org.clojure/clojure "1.2.1"]]
               :all  [[net.sf.ehcache/ehcache-core "2.4.7"]
                      [clj-cache "0.0.4"]]}
  :dev-dependencies [[swank-clojure "1.3.4"]
                     [log4j/log4j "1.2.13"]
                     [org.slf4j/slf4j-log4j12 "1.6.1"]
                     [clj-file-utils  "0.2.1"]]
  :warn-on-reflection false
  :jar-exclusions [#"log4j\.properties"]
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"})