(ns graphql.resolver)

(defn hello-graphql
  [_ _ _]
  (prn "hello, called!")
  "hello, world!")

(defn get-user
  [{:keys [db]
    :as   _ctx} {:keys [id]
                 :as   _variables} _parents]
  (prn "db->" db)
  {:id        id
   :firstName "tlonist"})

(def resolver-map
  {:queries/hello hello-graphql
   :queries/user  get-user})