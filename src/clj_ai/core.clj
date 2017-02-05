(ns clj-ai.core
  (:require [clojure.spec :as s]
            [clojure.pprint :refer [pprint]]))

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

(defn train
  "Accepts a matrix of link weights and some
  input data and returns the updates matrix
  of weights"
  [network input]
  {:network (s/valid? network)
   :input (s/valid? input)})

(defn query
  "Accepts a matrix of link weights an some
  input and returns the neural network output"
  [network input]
  (when-not (s/valid? ::network network)
    (throw (ex-info "The network is not valid" {:network network}))))
