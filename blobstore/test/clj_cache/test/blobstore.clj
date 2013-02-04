(ns clj-cache.test.blobstore
  (use clojure.test org.jclouds.blobstore2 clj-cache.cache)
  (require [clj-cache.blobstore :as blobstore]))

(defn clean-stub-fixture
  "This should allow basic tests to easily be run with another service."
  [blobstore]
  (fn [f]
    (doseq [container (containers blobstore)]
      (delete-container blobstore (.getName container)))
    (f)))

(def ^{:dynamic true} *blobstore*
  (blobstore "filesystem" "" "" :jclouds.filesystem.basedir "./tmp/blobstore")
  ;;(blobstore "transient" "" "")
  )

;;(use-fixtures :each (clean-stub-fixture *blobstore*))

(defn slow [a] (Thread/sleep a) a)

(def fast-default (cached slow (blobstore/put-if-absent-strategy *blobstore* "cache-container" {:gzip true})))

(defmacro how-long [expr]
  `(let [start# (. System (nanoTime))
         ret# ~expr
         msecs# (/ (double (- (. System (nanoTime)) start#))
                   1000000.0)]
     {:msecs msecs# :ret ret#}))

(defn expect [situation f check t should]
  (let [{:keys [msecs ret]} (how-long (f t))]
    (is (check msecs t) (str situation " (expected time:" t ", actual time: " msecs ") " should))
    (is (= ret t) (str situation " returns correct value"))))

(defn is-caching [f t]
  (invalidate-cache f t)
  (expect "First call" f > t "hits function" )
  (expect "Second call" f < t "is cached")
  (expect "Third call" f < t "is cached"))

(deftest is-caching-blobstore (is-caching fast-default 100))

(defn invalidating [f t1 t2 t3]
  (invalidate-cache f t1)
  (invalidate-cache f t2)
  (invalidate-cache f t3)
  (expect "First call" f > t1 "hits function")
  (expect "First call" f > t2 "hits function")
  (expect "First call" f > t3 "hits function")
  (invalidate-cache f t1)
  (expect "Invalidated entry" f > t1 "hits function")
  (expect "Second call" f < t2 "is cached")
  (expect "Second call" f < t3 "is cached")
  (expect "Third call" f < t1 "is cached"))

(deftest invalidating-hazelcast (invalidating fast-default 50 51 52))

(defn-cached cached-fn
  (blobstore/put-if-absent-strategy *blobstore* "another-container")
  "A cached function definition"
  [t]
  (doto t (Thread/sleep)))

(deftest is-caching-def (is-caching cached-fn 100))

(deftest correct-blob-naming
  (let [bs (blobstore "filesystem" "" "" :jclouds.filesystem.basedir "./tmp/blobstore")
        cached-fn (cached slow
                          (blobstore/put-if-absent-strategy bs "test-container")
                          {:cache-key #(str "my-num:" (first %))})
        _  (cached-fn 100)
        blobs (for [blob (blobs bs "test-container" :recursive true) :when (= "BLOB" (str (.getType blob)))]
                (str (.getUri blob)))]
    (is (= 1 (count blobs)))
    (is (= ["mem://test-container/user.slow/my-num:100"] blobs))))
