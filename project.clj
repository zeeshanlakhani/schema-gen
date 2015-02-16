(defproject schema-gen "0.1.4"
  :description "Turn Prismatic schemas into generated data using test.check."
  :url "https://github.com/zeeshanlakhani/schema-gen"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.gfredericks/test.chuck "0.1.14"]
                 [prismatic/schema "0.3.7"]
                 [org.clojure/test.check "0.7.0"]]
  :lein-release {:deploy-via :shell
                 :shell ["lein" "deploy" "clojars"]}
  :scm {:name "git"
        :url "https://github.com/zeeshanlakhani/schema-gen"})
