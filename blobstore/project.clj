(defproject clj-cache-blobstore "0.0.5"
  :description "JClould's BlobStore support for clj-cache"
  :repositories { "jclouds-snapshot" "https://oss.sonatype.org/content/repositories/snapshots"}
  :dependencies [[org.jclouds/jclouds-allblobstore "1.3.0"]
                 [org.clojure/tools.logging "0.2.3"]
                 [clj-cache "0.0.4"]]
  :multi-deps {"1.3" [[org.clojure/clojure "1.3.0"]]
               "1.2" [[org.clojure/clojure "1.2.1"]]
               :all [[org.jclouds/jclouds-allblobstore "1.3.0"]
                     [org.clojure/tools.logging "0.2.3"]
                     [clj-cache "0.0.4"]]}
  :dev-dependencies [[swank-clojure "1.4.0-SNAPSHOT"]]
  :warn-on-reflection true
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"})
