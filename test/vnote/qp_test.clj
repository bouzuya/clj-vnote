(ns vnote.qp-test
  (:require [vnote.qp :as qp])
  (:use clojure.test))

(deftest trim-end-test
  (are [input expected]
    (= (qp/trim-end input) expected)
    " " ""
    "  " ""
    "a " "a"
    "a  " "a"
    " a " " a"))

(deftest code-point->str-test
  (are [input expected]
    (= (qp/code-point->str input) expected)
    33 "!"
    60 "<"
    61 "="
    62 ">"
    126 "~"))

(deftest encode-test
  (is (= (qp/encode "") ""))
  (is (= (qp/encode " ") "=20"))
  (is (= (qp/encode "!") "!"))
  (is (= (qp/encode "<") "<"))
  (is (= (qp/encode "=") "=3D"))
  (is (= (qp/encode ">") ">"))
  (is (= (qp/encode "~") "~"))
  (is (= (qp/encode "\u3042") "=E3=81=82"))) ; FIXME support other encoding

(deftest decode-test
  (are [input expected]
    (= (qp/decode input) expected)
    "" ""
    "a" "a"
    "=20" " "
    "=3D" "="
    "=E3=81=82" "\u3042"))

