(ns clj-ai.core-test
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
            [clojure.core.matrix :refer [shape]]
            [clj-ai.core :refer :all]))

(def ^:private network
  [[[0.11M 0.12M]
    [0.13M 0.14M]]
   [[0.21M 0.22M]
    [0.23M 0.24M]]
   [[0.31M 0.32M]
    [0.33M 0.34M]]])

(deftest query-a-neural-network
  (let [input [5 9]]
    (is (= [2] (shape (query network input))) "The result is a vector of numbers")))

(deftest train-a-neural-network
  (let [result (train network [5M 3M] [0.05M 0.02M])]
    (is (vector? result) "A vector is returned")
    (is (= (shape network) (shape result)) "The result is the same shape as the input network")))

(deftest the-network-must-be-a-vector
  (is (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"The network is not valid"
        (query "network" [1M 2M 3M]))))

(deftest the-network-must-be-a-vector-of-vectors
  (is (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"The network is not valid"
        (query ["one" "two"] [1M 2M 3M]))))

(deftest the-input-must-be-a-vector-of-numbers
  (is (thrown-with-msg?
        clojure.lang.ExceptionInfo
        #"The input is not valid"
        (query network :not-a-vector))))
