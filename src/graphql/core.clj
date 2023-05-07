(ns graphql.core
  (:require [clojure.data.json :as json]
            [clojure.edn :as edn]
            [com.walmartlabs.lacinia :as lacinia]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [graphql.resolver :refer [resolver-map]]
            [ring.util.response :as rr]))

(defn compile-schemas [input]
  (-> (->> input
           (map slurp)
           (map edn/read-string)
           (apply merge))
      (util/inject-resolvers resolver-map)
      (schema/compile)))

(defn handler [db]
  (fn [request]
    (let [{:keys [query variable]} (-> request :body-params (update-keys keyword))
          schmeas                  (compile-schemas ["resources/graphql/queries.edn"
                                                     "resources/graphql/objects.edn"])
          ctx                      {:db db}
          result                   (lacinia/execute schmeas query variable ctx)]
      (-> result
          (json/write-str)
          (rr/response)))))

(defn routes [db]
  ["/graphql" {:post {:handler (handler db)}}])

(comment
  temp
  :rcf)