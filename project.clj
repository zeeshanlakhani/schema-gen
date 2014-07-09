(defproject schema-gen "0.1.1-SNAPSHOT"
  :description "Turn Prismatic schemas into generated data using test.check."
  :url "https://github.com/zeeshanlakhani/schema->gen"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.gfredericks/re-rand "0.1.1"]
                 [prismatic/schema "0.2.4"]
                 [org.clojure/test.check "0.5.8"]]
  :lein-release {:deploy-via :shell
                 :shell ["lein" "deploy" "clojars"]})
