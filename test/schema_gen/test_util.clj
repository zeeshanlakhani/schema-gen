(ns schema-gen.test-util
  (:require [clojure.test.check.clojure-test :as clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [schema-gen.core :refer [schema->gen]]
            [clojure.test :refer :all]))

;; Hack-ed macro, based off
;; http://dev.clojure.org/jira/browse/TCHECK-10?page=com.atlassian.streams.streams-jira-plugin:activity-stream-issue-tab
;; Waiting on max-size working w/ test.check defspec
(defmacro change-max-size
  [name default-test-count max-size]
  `(alter-meta! (var ~name) assoc
                :test (fn [] (#'clojure-test/assert-check
                              (assoc (~name ~default-test-count
                                            :max-size ~max-size)
                                :test-var (str '~name))))))

(defmacro deftest-gen
  {:requires [#'defspec]}
  [name default-times property size-map]
  `(do (defspec ~name ~default-times ~property)
       (change-max-size ~name ~default-times (:max-size ~size-map))))

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
