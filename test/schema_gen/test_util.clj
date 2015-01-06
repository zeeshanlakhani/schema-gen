(ns schema-gen.test-util
  (:require [clojure.test.check.clojure-test :as clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [schema-gen.core :refer [schema->gen]]
            [clojure.test :refer :all]))

(defmacro deftest-gen
  {:requires [#'defspec]}
  [test-name default-times property & props]
  (let [num-tests default-times
        {:keys [max-size seed] :or {max-size 20}} (first props)]
    `(defspec ~test-name {:num-tests ~num-tests
                          :max-size  ~max-size
                          :seed      ~seed} ~property)))

;; From Prismatic Schema
;; https://github.com/Prismatic/schema/blob/9d76dd2165f02f6e1d7ae9cb40fc7b39ee1ab25f/test/clj/schema/test_macros.clj#L7
(defmacro valid!
  "Assert that x satisfies schema s, and the walked value is equal to the original."
  [s x]
  `(let [x# ~x] (~'is (= x# ((s/start-walker s/walker ~s) x#)))))

(defmacro test-gen
  {:requires [#'prop/for-all schema->gen]}
  [schema]
  `(prop/for-all
    [gen# (schema->gen ~schema)]
    (testing "Validate Generated Data Against Schema"
      (valid! ~schema gen#))))
