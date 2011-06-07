(defproject clj-cache-blobstore "0.0.4-SNAPSHOT"
  :description "JClould's BlobStore support for clj-cache"
  :repositories { "jclouds-snapshot" "https://oss.sonatype.org/content/repositories/snapshots"}
  ;; TODO: remove above repo and lock to 1.0-beta-10c once it is released
  :dependencies [[org.jclouds/jclouds-allblobstore "1.0-SNAPSHOT"]
                 [clj-cache "0.0.4"]]
  :dev-dependencies [[org.clojure/clojure "1.2.0"]
                     [org.clojure/clojure-contrib "1.2.0"]
                     [swank-clojure "1.4.0-SNAPSHOT"]]
  :warn-on-reflection true
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"})

