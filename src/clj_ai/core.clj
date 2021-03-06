(ns clj-ai.core
  (:require [clojure.spec :as s]
            [clojure.pprint :refer [pprint]]
            [clojure.core.matrix :refer [mmul array]]))

(defn- network?
  [network]
  (when (and (vector? network)
             (every? vector? network))
    (let [row-count (-> network first count)
          col-count (-> network first first count)]
      (every? (fn [matrix]
                (and
                  (= row-count (count matrix))
                  (every? (fn [row]
                            (and (= col-count (count row))
                                 (every? number? row)))
                          matrix)))
              network))))

(s/def ::network network?)
(s/def ::input (s/coll-of number?))

(defn- sigmoid
  [value]
  (->> value
       (* value)
       (+ 1)
       Math/sqrt
       (/ value)))

(defn query
  "Accepts a matrix of link weights an some
  input and returns the neural network output

  network - a vector of matrices (vector of vectors) represending weights of links between nodes
  input - a vector a numbers"
  [network input]
  (when-not (s/valid? ::network network)
    (throw (ex-info "The network is not valid" {:network network})))
  (when-not (s/valid? ::input input)
    (throw (ex-info "The input is not valid" {:input input})))
  (reduce (fn [input layer]
            (->> input
                 (mmul (array layer))
                 (map sigmoid)))
          input
          network))

(defn train
  "Accepts a matrix of link weights and some
  input data and returns the updates matrix
  of weights

  network - a vector of matrices (vector of vectors) represending weights of links between nodes
  input - a vector a numbers
  target - a vector of numbers representing the desired response"
  [network input target]
  (when-not (s/valid? ::network network)
    (throw (ex-info "The network is not valid" {:network network})))
  (when-not (s/valid? ::input input)
    (throw (ex-info "The input is not valid" {:input input})))
  (when-not (s/valid? ::input target)
    (throw (ex-info "The target is not valid" {:target target})))
  (let [stack (reduce (fn [stack layer]
                        (conj stack {:layer layer
                                     :output (->> (:output (first stack))
                                                  (mmul (array layer))
                                                  (map sigmoid))}))
                      (list {:output input})
                      network)]
    stack))
