(ns vnote.core
  (:gen-class))

(defn parse-vnote
  [vnote-str]
  (->>
    vnote-str
    (re-seq #"(?m)^BODY[^:]*:(.*)$")
    (map second)
    first
    (#(.replaceAll % "\\Q=0D=0A\\E" "\r\n"))))

(defn -main
  [& args]
  (->
    (first args)
    slurp
    parse-vnote
    println))

