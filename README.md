
Cache dot clj
=============

 A Clojure library that caches the results of impure functions.

I have found this useful for caching the results of database calls and for holding HTML snippets.

This library is available at [clojars.org](http://clojars.org/) for use with Leiningen/Maven
     :dependencies [[uk.org.alienscience/cache-dot-clj "0.0.2"]]

It consists of small modifications to the memoization functions described in these two excellent blog posts, [the rule of three](http://kotka.de/blog/2010/03/The_Rule_of_Three.html) and [memoize done right](http://kotka.de/blog/2010/03/memoize_done_right.html). I'd recommend these posts to Clojure programmers as they discuss flexible apis and concurrency in real world detail.


Example
-------

    (ns an-example
      (:use cache-dot-clj.cache))

    (defn-cached get-user-from-db 
      (lru-cache-strategy 1000)
      "Gets a user details from a database. Caches the last 1000
       users read in i.e support serving a 1000 concurrent users
       from memory."
      [username]
      ;; Slow database read goes here
    )

    (def get-user-from-db 
      (cached get-user-from-db (lru-cache-strategy 1000)))

    ;; First read of the user is slow
    (get-user-from-db "fred")
 
    ;; Second is fast
    (get-user-from-db "fred")

    ;; When fred's details are changed invalidate the cache
    (invalidate-cache get-user-from-db "fred")

    ;; The next call will read from the db and cache the result again
    (get-user-from-db "fred")

Available Algorithms
--------------------

    ;; Cache all calls with no limits
    naive-strategy

    ;; Least Recently Used
    (lru-cache-strategy cache-size)

    ;; Time to live
    (ttl-cache-strategy time-to-live-millisecs)

    ;; Least used
    (lu-cache-strategy cache-size)

I've found the Least Recently Used (LRU) algorithm to be the most robust for web applications.

Available Functions
-------------------

### cached

    ([f strategy])
     Returns a cached function that can be invalidated by calling
     invalidate-cache e.g
      (def fib (cached fib (lru-cache-stategy 5)))

### invalidate-cache

    ([cached-f & args])
     Invalidates the cache for the function call with the given arguments
     causing it to be re-evaluated e.g
      (invalidate-cache fib 30)  ;; A call to (fib 30) will not use the cache
      (invalidate-cache fib 29)  ;; A call to (fib 29) will not use the cache
      (fib 18)                   ;; A call to (fib 18) will use the cache

Available Macros
----------------

### defn-cached

    ([fn-name cache-strategy & defn-stuff])
    Macro
    Defines a cached function, like defn-memo from clojure.contrib.def
    e.g
     (defn-cached fib
        (lru-cache-strategy 10)
        [n]
        (if (<= n 1)
          n
          (+ (fib (dec n)) (fib (- n 2)))))
