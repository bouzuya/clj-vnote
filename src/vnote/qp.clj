(ns vnote.qp
  "Quoted-Printable for VNOTE. This is NOT equal to RFC2045's Quoted-Printable.")

(defn trim-end
  [s]
  (.replaceAll s " +$" ""))

(defn utf8-bytes->string
  [bs]
  (String. (byte-array (map byte bs)) "UTF-8"))

(defn byte->hex
  [b]
  (->
    b
    (bit-and 0xFF)
    (Integer/toHexString)
    (.toUpperCase)))

(defn hex->byte
  [s]
  (->
    s
    (Integer/valueOf 16)
    (.byteValue)))

(defn code-point->str
  [code-point]
  (String. (Character/toChars code-point)))

(defn encode
  [s]
  (->>
    s
    (#(.getBytes % "UTF-8"))
    (map (fn [b]
           (if (<= 33 b 60)         ; !-< 33-60
             (code-point->str b)
             (if (<= 62 b 126)      ; >-~ 62-126
               (code-point->str b)
               (if (= 61 b)         ; = 61
                 "=3D"
                 (format "=%02X" b)))))) ; others
    (apply str)))

(defn decode
  [s]
  (loop [s (partition-all 3 1 (.getBytes s "UTF-8"))
         bs []]
    (if (empty? s)
      (utf8-bytes->string bs)
      (if (= (ffirst s) (byte (int \=)))
        (recur (next (next (next s)))
               (conj bs (let [[x y z] (first s)]
                          (if (or (nil? y) (nil? z))
                            (byte 61) ; \=
                            (hex->byte (str (char y) (char z)))))))
        (recur (rest s) (conj bs (byte (int (ffirst s)))))))))

