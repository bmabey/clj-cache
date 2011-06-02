(ns clj-cache.blobstore
  (use org.jclouds.blobstore2)
  (import  [java.io File ObjectOutputStream ObjectInputStream Serializable]
           [java.util.zip GZIPOutputStream GZIPInputStream]))

(defn- obj->stream-fn
  "Takes an object returning a function that will write (using Java serialization) the object to the given OutputStream."
  [^Serializable obj & [options]]
  (fn [output-stream]
    (let [os (if (:gzip options) (GZIPOutputStream. output-stream) output-stream)]
      (with-open [out (ObjectOutputStream. os)]
        (.writeObject out obj)))))

(defn stream->obj
  "Takes an InputStream and deserializes it into an Object."
  [input-stream & [options]]
  (let [is (if (:gzip options) (GZIPInputStream. input-stream) input-stream)]
    (with-open [in (ObjectInputStream. is)]
      (.readObject in))))

(defn assoc-if-absent
  "Looks up the map by name and assocs the k/v pair only if the key is absent."
  [{:keys [blobstore container path serialization-options]} key value]
  (when-not (blob-exists? blobstore container (path key))
    (put-blob blobstore container (blob (path key) :payload (obj->stream-fn value serialization-options))))
  value)

(defn lookup
  "Looks up an item in the given cache. Returns a vector:
[element-exists? value]"
  [{:keys [blobstore container path serialization-options]} key]
  (if (blob-exists? blobstore container (path key))
    [true (-> (get-blob-stream blobstore container (path key))
              (stream->obj serialization-options))]
    [false nil]))

(defn invalidate
  "Removes an item from the cache."
  [{:keys [blobstore container path]} key]
  (remove-blob blobstore container (path key)))

(defn put-if-absent-strategy [blobstore container & [serialization-options]]
  {:init (fn [fn-name]
           (create-container blobstore container)
           (create-directory blobstore container fn-name)
           {:blobstore blobstore
            :container container
            :path (fn [key] (str fn-name "/" key))
            :serialization-options serialization-options})
   :lookup lookup
   :miss! assoc-if-absent
   :invalidate! invalidate
   :description "JCloulds Blobstore backend"
   :plugs-into :external-memoize})
