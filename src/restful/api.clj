(ns restful.api
  (:require [compojure.core :as c]
            [compojure.route :as route]
            [clojure.pprint :as pp]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [restful.client :as client]))

(def people-collection (atom []))

(defn add-person [firstname surname]
  (swap! people-collection conj {:firstname (str/capitalize firstname) :surname (str/capitalize surname)}))

(defn get-parameters [req pname] (get (:params req) pname))

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

(defn add-person-handler [req]
  {:status 200
   :headers {"Content-Type" "text/json"}
   :body (-> (let [p (partial get-parameters req)]
               (str (json/write-str (add-person (p :firstname) (p :surname))))))})

(defn people-handler [_]
  {:status 200
   :headers {"Content-Type" "text/json"}
   :body (str (json/write-str @people-collection))})

(defn add-book-handler [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (-> (let [b (partial get-parameters req)]
               (client/put {:book/title  (b :title)
                            :book/author (b :author)
                            :book/genre  (b :genre)})
               (client/all-books)))})

(defn books-handler [_]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body  (client/all-books)})

(c/defroutes app-routes
  (c/GET "/" [] simple-body-page)
  (c/GET "/request" [] request-example)
  (c/GET "/hello" [] hello-name)
  (c/GET "/people" [] people-handler)
  (c/GET "/people/add" [] add-person-handler)
  (c/GET "/books/add" [] add-book-handler)
  (c/GET "/books" [] books-handler)
  (route/not-found "Error, page not found!"))