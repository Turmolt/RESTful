(ns restful.client
  (:require [datomic.client.api :as d]))

(def config {:server-type        :peer-server
             :access-key         "myaccesskey"
             :secret             "mysecret"
             :endpoint           "localhost:8998"
             :validate-hostnames false})

(def client (d/client config))

(def conn (d/connect client {:db-name "books"}))

(defn db [] (d/db conn))

(def book-schema [{:db/ident       :book/title
                   :db/valueType   :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc         "The title of the book"}

                  {:db/ident       :book/author
                   :db/valueType   :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc         "The author of the book"}

                  {:db/ident       :book/genre
                   :db/valueType   :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc         "The genre of the book"}])

(def schema (d/transact conn {:tx-data book-schema}))

(defn put [data] (d/transact conn {:tx-data [data]}))

(def all-books-q '[:find ?title ?author ?genre
                   :where [?e :book/title  ?title]
                   [?e :book/author ?author]
                   [?e :book/genre  ?genre]])

(def all-books-t '[:find (pull ?e [*])
                   :where [?e :book/title  ?title]])

(defn all-books [] (d/q all-books-t (db)))

(defn all-unique-titles []
  (let [books (d/q all-books-t (db))]
    (-> (fn [[entry]] 
          (dissoc entry :db/id))
        (map books)
        (set))))