(ns schema-gen.core-test
  (:require [schema.core :as s]
            [schema-gen.test-util :refer [deftest-gen test-gen]]
            [clojure.test :refer :all]))

(def s-vector
  [(s/one s/Bool "first") (s/one s/Num "second") (s/optional s/Keyword "maybe") s/Int])

(def s-hashmap
  {:foo s/Int
   (s/optional-key :schema-gen) s/Bool
   :baz s/Str
   s/Keyword s/Num})

(def s-hashmap-with-hashmap
  {:foo s/Int
   :baz s/Str
   :bar {:foo s/Int}
   s/Keyword s/Num})

(def s-hashmap-with-vector
  {:foo s/Int
   (s/optional-key :schema-gen) s/Bool
   :baz [s/Str]
   s/Keyword s/Num})

(def test-property-vector
  (test-gen s-vector))

(def test-property-hashmap
  (test-gen s-hashmap))

(def test-property-hashmap-2
  (test-gen s-hashmap-with-hashmap))

(def test-property-hashmap-3
  (test-gen s-hashmap-with-vector))

(deftest-gen test-vector 100 test-property-vector {:max-size 20})
(deftest-gen test-hashmap 100 test-property-hashmap {:max-size 20})
(deftest-gen test-hashmap-2 100 test-property-hashmap-2 {:max-size 20})
(deftest-gen test-hashmap-3 100 test-property-hashmap-3 {:max-size 20})
