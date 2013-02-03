(ns vnote.core-test
  (:use clojure.test
        vnote.core))

(deftest parse-vnote-test
  (is
    (= (parse-vnote (str "BEGIN:VNOTE\n"
                         "VERSION:1.1\n"
                         "BODY;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:good morning=0D=0Ahello=0D=0Agood night\n"
                         "DCREATED:20130130T231151\n"
                         "LAST-MODIFIED:20130130T231151\n"
                         "END:VNOTE\n"))
       {:VERSION "1.1"
        :BODY "good morning\r\nhello\r\ngood night"
        :DCREATED "20130130T231151"
        :LAST-MODIFIED "20130130T231151"}))
  (is
    (= (parse-vnote (str "BEGIN:VNOTE\n"
                         "VERSION:1.2\n"
                         "BODY;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:abc=0D=0A\n"
                         "DCREATED:20130201T000000\n"
                         "LAST-MODIFIED:20130201T000001\n"
                         "END:VNOTE\n"))
       {:VERSION "1.2"
        :BODY "abc\r\n"
        :DCREATED "20130201T000000"
        :LAST-MODIFIED "20130201T000001"}))
  (is
    (= (parse-vnote (str
                      "BEGIN:VNOTE\n"
                      "VERSION:1.1\n"
                      "BODY;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:改行=0D=0A空白 空白 =0D=0Aおわり\n"
                      "DCREATED:20130202T092519\n"
                      "LAST-MODIFIED:20130202T092519\n"
                      "END:VNOTE\n"))
       {:VERSION "1.1"
        :BODY "改行\r\n空白 空白 \r\nおわり"
        :DCREATED "20130202T092519"
        :LAST-MODIFIED "20130202T092519"})))



