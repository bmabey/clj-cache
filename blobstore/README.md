# JClouds BlobStore support for clj-cache


This plugin allows clj-cache to be used with [JCloud's BlobStore](http://code.google.com/p/jclouds/wiki/BlobStore) to allow for distributed and persistent caching.


## Example

Right now only a very simple strategy is provided that uses `putIfAbsent` with no locking/blocking:

    (ns an-example
      (use clj-cache.cache
           org.jclouds.blobstore2)
      (require [clj-cache.blobstore]))

    (def my-blobstore (blobstore "transient" "" "")) ;; see JCloud's docs for creating other blobstores (e.g S3)

    (defn-cached process-large-ammounts-of-data
      (clj-cache.blobstore/put-if-absent-strategy my-blobstore "cache-container-name" {:gzip true})
      {:cache-key (fn [[data]] (data->str-name data))}
      [data]
      ;; Slow data processing goes here
    )

## Dependencies

To use JCloud's BlobStore and clj-cache pull in the following dependency using Leiningen, Cake or Maven:

     [clj-cache-blobstore "0.0.4"]

## Limitations

This package assumes all cache keys can be safely and uniquely `str`ed and values are `java.io.Serializable`. This covers most clojure datastructures but means that different versions of clojure (e.g 1.1 and 1.2) shouldn't share the same distributed cache.
If you need to control how your key is converted to a string then you can pass in a `:cache-key` function as shown above.


As stated above the `put-if-absent-strategy` does not lock/block when being populated so wasted computation may occur.  No locking/blocking strategy is available for this plugin.
