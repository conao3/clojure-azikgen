(ns azikgen.core
  (:require
   [clj-yaml.core :as yaml]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log])
  (:gen-class))

(defn handler-matrix-rule [step]
  (->> (mapcat (fn [[key val]]
                 (map
                  (fn [b v] (when (some? v) [(str key b) v]))
                  (:base step)
                  val))
               (:matrix step))
       (into {})))

(defn generate-1 [rules step]
  (condp = (:type step)
    "matrix-rule"
    (handler-matrix-rule step)))

(defn generate
  ([steps]
   (generate nil steps))
  ([rules steps]
   (if (next steps)
     (recur (generate-1 rules (first steps)) (next steps))
     (generate-1 rules (first steps)))))

(defn -main
  "The entrypoint."
  [& args]
  (log/info args)
  (->> (first args)
       io/reader
       yaml/parse-stream
       generate
       (into (sorted-map))
       json/json-str
       println))
