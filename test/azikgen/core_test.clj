(ns azikgen.core-test
  (:require
   [clojure.test :as t]
   [azikgen.core :as sut]))

(t/deftest sample-test
  (t/is (= true true)))

(t/deftest generate-test
  (t/is (thrown-with-msg? Exception #"a" (sut/generate nil))))

(t/deftest handler-matrix-rule-test
  (t/is (= {} (sut/handler-matrix-rule nil)))

  (t/is (= {"a" "あ"
            "i" "い"
            "u" "う"
            "e" "え"
            "o" "お"
            "ka" "か"
            "ki" "き"
            "ku" "く"
            "ke" "け"
            "ko" "こ"}
           (sut/handler-matrix-rule
            {:type "matrix-rule"
             :base ["a" "i" "u" "e" "o"]
             :matrix {"" ["あ" "い" "う" "え" "お"]
                      "k" ["か" "き" "く" "け" "こ"]}}))))

(t/deftest handler-postfix-rule-test
  (t/is (= {} (sut/handler-postfix-rule nil)))

  (t/is (= {"kya" "きゃ"
            "kyi" "きぃ"
            "kyu" "きゅ"
            "kye" "きぇ"
            "kyo" "きょ"
            "sya" "しゃ"
            "syi" "しぃ"
            "syu" "しゅ"
            "sye" "しぇ"
            "syo" "しょ"}
           (sut/handler-postfix-rule
            {:type "postfix-rule"
             :postfix {:a "ゃ" :i "ぃ" :e "ぇ" :u "ゅ" :o "ょ"}
             :matrix {:ky "き" :sy "し"}}))))

(t/deftest handler-raw-rule-test
  (t/is (= {} (sut/handler-raw-rule nil)))

  (t/is (= {"fu" "ふ"}
           (sut/handler-raw-rule {:type "raw-rule"
                                  :matrix {:fu "ふ"}}))))

(t/deftest handler-dependent-macro-rule-test
  (t/is (= {} (sut/handler-dependent-macro-rule nil nil)))

  (t/is (= {"kz" "かん"
            "kn" "かん"
            "kk" "きん"
            "kj" "くん"
            "kd" "けん"
            "kl" "こん"
            "sz" "さん"
            "sn" "さん"
            "sk" "しん"
            "sj" "すん"
            "sd" "せん"
            "sl" "そん"}
           (sut/handler-dependent-macro-rule
            {"ka" "か"
             "ki" "き"
             "ku" "く"
             "ke" "け"
             "ko" "こ"
             "sa" "さ"
             "si" "し"
             "su" "す"
             "se" "せ"
             "so" "そ"
             "nn" "ん"}
            {:type "dependent-macro-rule"
             :macro {:z "ann" :n "ann" :k "inn" :j "unn" :d "enn" :l "onn"}
             :pre ["k", "s"]}))))

(t/deftest apply-rule-test
  (let [rules {"a" "あ"
               "i" "い"
               "ka" "か"}]
    (t/is (= ["あ"]
             (sut/apply-rule rules "a")))

    (t/is (= ["か" "い"]
             (sut/apply-rule rules "kai")))

    (t/is (thrown? Exception
           (sut/apply-rule rules "o")))))
