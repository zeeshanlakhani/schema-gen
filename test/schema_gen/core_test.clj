(ns schema-gen.core-test
  (:require [schema.core :as s]
            [schema-gen.core :refer :all]
            [schema-gen.test-util :refer [deftest-gen test-gen]]
            [clojure.test :refer :all]
            [clojure.test.check.generators :as gen])
  (:import clojure.lang.ExceptionInfo))

(def s-basic-vector
  [s/Keyword])

(def s-vector
  [(s/one s/Bool "first")
   (s/one s/Num "second")
   (s/one #"[a-z0-9]" "third")
   (s/optional s/Keyword "maybe")
   s/Int])

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

(def s-hashmap-with-predicate
  {:foo s/Int
   :baz (s/pred empty?)})

(def s-hashmap-with-buried-predicate
  {:foo s/Int
   :baz {:bar s/Str
         :quux (s/pred empty?)}})

(def test-property-basic-vector
  (test-gen s-basic-vector))

(def test-property-vector
  (test-gen s-vector))

(def test-property-hashmap
  (test-gen s-hashmap))

(def test-property-hashmap-2
  (test-gen s-hashmap-with-hashmap))

(def test-property-hashmap-3
  (test-gen s-hashmap-with-vector))

(deftest-gen test-basic-vector 100 test-property-basic-vector
  {:max-size 100 :seed 100})
(deftest-gen test-vector 200 test-property-vector)
(deftest-gen test-hashmap 100 test-property-hashmap)
(deftest-gen test-hashmap-2 100 test-property-hashmap-2)
(deftest-gen test-hashmap-3 100 test-property-hashmap-3)

(deftest test-predicate-error-msg
  (is (thrown-with-msg? ExceptionInfo #":baz"
                        (schema->gen s-hashmap-with-predicate)))
  (is (= [:baz :quux] (-> (try (schema->gen s-hashmap-with-buried-predicate)
                               (catch ExceptionInfo e e))
                          ex-data
                          :key-path))))

;; regression test
(deftest test-maybe-regex
  (is (gen/generator? (schema->gen (s/maybe #"abc")))))
