(ns restful.core
  (:require [org.httpkit.server :as server]
            [ring.middleware.defaults :as d]
            [restful.api :as api]
            [restful.client :as client])
  (:gen-class))

(defn -main
  "Start a RESTful server"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server (d/wrap-defaults #'api/app-routes d/site-defaults) {:port port})
    (println (str "Running server at http://localhost:" port "/"))))
