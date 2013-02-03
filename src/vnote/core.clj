(ns vnote.core
  (:require [vnote.qp :as qp])
  (:gen-class))

(defn gen-map
  [src split-line-fn split-key-value-fn]
  (into {} (map split-key-value-fn (split-line-fn src))))

(defn gen-kv
  [key-fn val-fn]
  (fn [[k v _]] [(key-fn k) (val-fn v)]))

(def ignore-kv (fn [_] nil))

(defn gen-body-kv
  [opts]
  ; TODO support other opts
  (if (and (= "UTF-8" (:CHARSET opts))
           (= "QUOTED-PRINTABLE" (:ENCODING opts)))
    (gen-kv keyword qp/decode)
    ignore-kv))

(def kv (gen-kv keyword identity))

(defn parse-vnote
  [vnote-str]
  (gen-map
    vnote-str
    (fn [src] (clojure.string/split src #"\n"))
    (fn [line]
      (let [[k-raw v] (clojure.string/split line #":")
            [k & k-opts] (clojure.string/split k-raw #";")
            opts (gen-map k-opts identity #(kv (clojure.string/split % #"=")))
            kv-fn (case k
                    "BEGIN" ignore-kv
                    "END" ignore-kv
                    "BODY" (gen-body-kv opts)
                    kv)]
        (kv-fn [k v])))))

(defn -main
  [& args]
  (->
    (first args)
    slurp
    parse-vnote
    :BODY
    println))

