(ns core
  (:require [integrant.core :as ig]
            [io.pedestal.http :as http]
            [next.jdbc :as jdbc]
            [service :as service]))

(defmethod ig/init-key :db/mysql
  [_name config]
  (prn "initializing mysql db")
  (jdbc/get-datasource config))

(defmethod ig/init-key :c24/app
  [_name config]
  (prn "initializing pedestal app")
  (-> (service/service-map config)
      (http/create-server)))

(defmethod ig/init-key :server/pedestal
  [_name {:keys [app]}]
  (prn "initializing pedestal server")
  (http/start app))

(defmethod ig/halt-key! :server/pedestal
  [_name server]
  (prn "halting pedestal server")
  (http/stop server))

(comment
  (->> "resources/config.edn"
       (slurp)
       (ig/read-string)
       (ig/init))
  :rcf)