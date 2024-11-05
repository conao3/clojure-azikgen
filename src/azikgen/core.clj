(ns azikgen.core
  (:require
   [clj-yaml.core :as yaml]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log])
  (:gen-class))

(defn generate [rules defs]
  rules)

(defn -main
  "The entrypoint."
  [& args]
  (log/info args)
  (-> (first args)
      io/reader
      yaml/parse-stream
      (->> (generate nil))
      json/json-str
      println))
