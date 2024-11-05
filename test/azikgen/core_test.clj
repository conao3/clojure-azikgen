(ns azikgen.core-test
  (:require
   [clojure.test :as t]
   [azikgen.core :as sut]))

(t/deftest sample-test
  (t/is (= true true)))

(t/deftest generate-test
  (t/is (= nil (sut/generate nil nil))))
