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
