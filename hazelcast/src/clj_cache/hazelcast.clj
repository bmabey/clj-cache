(ns clj-cache.hazelcast
  (:import com.hazelcast.core.Hazelcast))


;; These functions do not take a Hazelcast map, but rather a name for a map that can be used to look up the map.
;; The motivation behind this was to allow the Hazelcast maps (and potentially the Hazelcast instance) to be lazily
;; created.  This way maps are only created when they need to be.

(defn assoc-if-absent
  "Looks up the map by name and assocs the k/v pair only if the key is absent."
  [map-name key value]
  (-> (Hazelcast/getMap map-name) (.putIfAbsent key value))
  value)

(defn lookup
  "Looks up an item in the given cache. Returns a vector:
element-exists? value]"
  [map-name key]
  (let [value (-> (Hazelcast/getMap map-name) (.get key))]
    [(-> value nil? not) value]))

(defn invalidate
  "Removes an item from the cache."
  [map-name key]
  (-> (Hazelcast/getMap map-name) (.remove ^Object key)))

(defn put-if-absent-strategy []
  {:init identity
   :lookup lookup
   :miss! assoc-if-absent
   :invalidate! invalidate
   :description "Hazelcast backend"
   :plugs-into :external-memoize})
