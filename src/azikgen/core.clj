(ns azikgen.core
  (:require
   [clj-yaml.core :as yaml]
   [clojure.data.json :as json]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log])
  (:gen-class))

(defn apply-rule [rules inpt]
  (let [len (count inpt)]
    (when (seq inpt)
      (or
       (and (<= 1 len) (when-let [t (get rules (subs inpt 0 1))] (cons t (apply-rule rules (subs inpt 1)))))
       (and (<= 2 len) (when-let [t (get rules (subs inpt 0 2))] (cons t (apply-rule rules (subs inpt 2)))))
       (and (<= 3 len) (when-let [t (get rules (subs inpt 0 3))] (cons t (apply-rule rules (subs inpt 3)))))
       (and (<= 4 len) (when-let [t (get rules (subs inpt 0 4))] (cons t (apply-rule rules (subs inpt 4)))))
       (and (<= 5 len) (when-let [t (get rules (subs inpt 0 5))] (cons t (apply-rule rules (subs inpt 5)))))
       (throw (ex-info (format "Cannot interpret %s using current rule set" inpt) {}))))))

(defn handler-matrix-rule [step]
  (->> (mapcat (fn [[key val]]
                 (map
                  (fn [b v] (when (some? v) [(str (name key) b) v]))
                  (:base step)
                  val))
               (:matrix step))
       (into {})))

(defn handler-postfix-rule [step]
  (->> (for [[kk kv] (:matrix step)
             [pk pv] (:postfix step)]
         [(str (name kk) (name pk)) (str kv pv)])
       (into {})))

(defn handler-raw-rule [step]
  (-> (:matrix step)
      (update-keys name)))

(defn handler-dependent-macro-rule [rules step]
  (->> (for [p (:pre step)
             [mk mv] (:macro step)]
         (when-let [applied (try
                              (apply-rule rules (str p mv))
                              (catch Exception e
                                (log/warn (ex-message e))
                                nil))]
           [(str p (name mk)) (apply str applied)]))
       (into {})))

(defn generate-1 [rules step]
  (merge
   rules
   (condp = (:type step)
     "matrix-rule" (handler-matrix-rule step)
     "postfix-rule" (handler-postfix-rule step)
     "raw-rule" (handler-raw-rule step)
     "dependent-macro-rule" (handler-dependent-macro-rule rules step))))

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
