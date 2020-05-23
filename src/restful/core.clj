(ns restful.core
  (:require [org.httpkit.server :as server]
            [compojure.core :as c]
            [compojure.route :as route]
            [ring.middleware.defaults :as d]
            [clojure.pprint :as pp]
            [clojure.data.json :as json]
            [clojure.string :as str])
  (:gen-class))

(def people-collection (atom []))

(defn addperson [firstname surname]
  (swap! people-collection conj {:firstname (str/capitalize firstname) :surname (str/capitalize surname)}))

(defn getparameter [req pname] (get (:params req) pname))

(defn simple-body-page [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hi mom"})

(defn request-example [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (->>
          (pp/pprint req)
          (str "Request Objejct: " req))})

(defn hello-name [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> (pp/pprint req)
             (str "Hello " (:name (:params req))))})

(defn addperson-handler [req]
  {:status 200
   :headers {"Content-Type" "text/json"}
   :body (-> (let [p (partial getparameter req)]
               (str (json/write-str (addperson (p :firstname) (p :surname))))))})

(defn people-handler [_]
  {:status 200
   :headers {"Content-Type" "text/json"}
   :body (str (json/write-str @people-collection))})

(c/defroutes app-routes
  (c/GET "/" [] simple-body-page)
  (c/GET "/request" [] request-example)
  (c/GET "/hello" [] hello-name)
  (c/GET "/people" [] people-handler)
  (c/GET "/people/add" [] addperson-handler)
  (route/not-found "Error, page not found!"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server (d/wrap-defaults #'app-routes d/site-defaults) {:port port})
    (println (str "Running server at http://localhost:"port"/"))))

