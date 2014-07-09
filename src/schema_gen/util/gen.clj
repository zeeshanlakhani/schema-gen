(ns schema-gen.util.gen
  (:require [clojure.test.check.generators :as gen]))

(defn apply-by
  [f & args]
  (gen/fmap f (apply gen/tuple (flatten args))))

(defn one-of
  [& args]
  (gen/one-of args))
