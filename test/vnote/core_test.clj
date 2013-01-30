(ns vnote.core-test
  (:use clojure.test
        vnote.core))

(deftest parse-vnote-test
  (is (= (parse-vnote (str "BEGIN:VNOTE\n"
                           "VERSION:1.1\n"
                           "BODY;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:good morning=0D=0Ahello=0D=0Agood night\n"
                           "DCREATED:20130130T231151\n"
                           "LAST-MODIFIED:20130130T231151\n"
                           "END:VNOTE\n"))
         "good morning\r\nhello\r\ngood night")))

