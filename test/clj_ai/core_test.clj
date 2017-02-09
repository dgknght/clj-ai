(ns clj-ai.core-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [clj-ai.core :refer :all]))

(def ^:private network
  [[[0.11 0.12]
    [0.13 0.14]]
   [[0.21 0.22]
    [0.23 0.24]]
   [[0.31 0.32]
    [0.33 0.34]]])

(deftest query-a-neural-network
  (let [input [5 9]]
    (pprint (query network input))))

(deftest the-network-must-be-a-vector
  (is (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"The network is not valid"
        (query "network" [1 2 3]))))

(deftest the-network-must-be-a-vector-of-vectors
  (is (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"The network is not valid"
        (query ["one" "two"] [1 2 3]))))

(deftest the-input-must-be-a-vector-of-numbers
  (is (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"The input is not valid"
        (query network :not-a-vector))))
