(defproject restful "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [http-kit "2.3.0"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/data.json "0.2.6"]
                 [com.datomic/client-pro "0.9.57"]]
  :main ^:skip-aot restful.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
