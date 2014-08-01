(ns schema-gen.core
  (:require [schema-gen.util.gen :as g]
            [four.stateful :as four]
            [re-rand :refer [re-rand]]
            [schema.core :as s]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.rose-tree :as rose]
            [clojure.string :as string])
  (:import clojure.lang.ExceptionInfo))

(declare schema->gen)
(declare schema->gen*)

(derive clojure.lang.PersistentArrayMap ::gen-map)
(derive clojure.lang.PersistentHashMap ::gen-map)

(defn optional-key-gen
  "Return (or not return) an optional-key."
  [[k v]]
  (g/one-of
   (gen/return {})
   (gen/fmap (partial apply hash-map)
             (gen/tuple (schema->gen k) (schema->gen v)))))

(defn re-randify-regex
  "Schema requires ^$ while re-rand forbids them."
  [re]
  (let [s (str re)]
    (if (re-matches #"\^.*\$" s)
      (re-pattern (subs s 1 (dec (count s))))
      re)))

(defn methods-supported
  []
  (->> schema->gen
     methods
     (merge (methods schema->gen*))
     (map first)
     (remove #(= % :default))))

(defmulti schema->gen  identity)
(defmulti schema->gen* class)

(defmethod schema->gen :default
  [e]
  (or
   (schema->gen* e)
   (if (s/schema-name e)
     {:$ref (s/schema-name e)}
     (throw (ex-info (str "\nSchema type not implemented - " (class e)
                          "\nOnly these methods are implemented: "
                          (apply list (methods-supported)))
                     {:type (class e)
                      :root true})))))

(defmethod schema->gen* :default
  [e])

(defmethod schema->gen s/Keyword
  [_]
  gen/keyword)

(defmethod schema->gen s/Bool
  [_]
  (gen/elements [true false]))

(defmethod schema->gen s/Int
  [_]
  gen/int)

(defmethod schema->gen s/Num
  [_]
  (gen/fmap float gen/ratio))

(defmethod schema->gen s/Str
  [_]
  gen/string-ascii)

(defmethod schema->gen* ::gen-map
  [e]
  (let [required (for [[k v] e
                       :when (or (keyword? k)
                                 (instance? schema.core.RequiredKey k))]
                   [k v])
        rest (apply dissoc e (map first required))
        [optional [repeated]] (split-with
                               (fn [[k v]]
                                 (instance? schema.core.OptionalKey k))
                               rest)]
    (g/apply-by
     (partial apply merge)
     (g/apply-by
      (partial into {})
      (map optional-key-gen optional))
     (if repeated
       (->> repeated (map schema->gen) (apply gen/map))
       (gen/return {}))
     (apply gen/hash-map
            (mapcat (fn [[k v]]
                      (try
                        [k (schema->gen v)]
                        (catch ExceptionInfo e
                          (if (-> e ex-data :root)
                            (throw (ex-info (str
                                             "Schema type " (type v)
                                             " not implemented for key " k)
                                            (assoc (ex-data e)
                                              :key k
                                              :root false)))
                            (throw (ex-info (.getMessage e)
                                            (let [ex-data (ex-data e)
                                                  key-path
                                                  (or (:key-path ex-data)
                                                     (list (:key ex-data)))]
                                              (assoc ex-data :key-path
                                                     (conj key-path k)))))))))
                    required)))))

(defmethod schema->gen* clojure.lang.Sequential
  [e]
  (let [[ones [repeated]] (split-with #(instance? schema.core.One %) e)
        [required optional] (split-with (comp not :optional?) ones)]
    (g/apply-by
     (partial apply concat)
     (g/one-of
      (apply gen/tuple (map schema->gen required))
      (g/apply-by
       (partial apply concat)
       (apply gen/tuple (map schema->gen (concat required optional)))
       (if repeated
         (gen/vector (schema->gen repeated))
         (gen/return [])))))))

(defmethod schema->gen* schema.core.One
  [e]
  (schema->gen (:schema e)))

(defmethod schema->gen* schema.core.RequiredKey
  [e]
  (gen/return (:k e)))

(defmethod schema->gen* schema.core.OptionalKey
  [e]
  (gen/return (:k e)))

(defmethod schema->gen* schema.core.Maybe
  [e]
  (gen/one-of
   [(gen/return nil)
    (schema->gen (:schema e))]))

(defmethod schema->gen* schema.core.EqSchema
  [e]
  (gen/return (:v e)))

(defmethod schema->gen* schema.core.AnythingSchema
  [_]
  gen/any-printable)

(defmethod schema->gen* schema.core.Either
  [e]
  (gen/one-of
   (map schema->gen (:schemas e))))

(defmethod schema->gen* schema.core.EnumSchema
  [e]
  (gen/elements (seq (:vs e))))

(defmethod schema->gen* schema.core.NamedSchema
  [e]
  (schema->gen (:schema e)))

(defmethod schema->gen* java.util.regex.Pattern
  [e]
  (let [re (re-randify-regex e)]
    {:gen (fn [r _size]
            (binding [four/*rand* r]
              (rose/pure (re-rand re))))}))
