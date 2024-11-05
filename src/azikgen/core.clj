(ns azikgen.core
  (:require
   [clj-yaml.core :as yaml]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log])
  (:gen-class))

(defn generate-1 [rules step]
  rules)

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
  (-> (first args)
      io/reader
      yaml/parse-stream
      generate
      json/json-str
      println))
