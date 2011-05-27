# Hazelcast support for clj-cache


This plugin allows clj-cache to be used with the java [Hazelcast](http://hazelcast.com/) library to provide distributed caching.


## Example

Right now only a very simple strategy is provided that uses `putIfAbsent` with no locking/blocking:

    (ns an-example
      (:use clj-cache.cache)
      (:require [clj-cache.hazelcast]))

    (defn-cached get-user-from-db
      (clj-cache.hazelcast/put-if-absent-strategy)
      [username]
      ;; Slow database read goes here
    )

## Dependencies

To use Hazelcast and clj-cache pull in the following dependency using Leiningen, Cake or Maven:

     [clj-cache-hazelcast "0.0.4"]

## Limitations

This package assumes all keys and values in the cache are `java.io.Serializable`. This covers most clojure datastructures but means that different versions of clojure (e.g 1.1 and 1.2) shouldn't share the same distributed cache.

As stated above the `put-if-absent-strategy` does not lock/block when being populated wasted computation may result.  No locking/blocking strategy is available for this plugin yet but Hazelcast supports locking so this can be added easily.
