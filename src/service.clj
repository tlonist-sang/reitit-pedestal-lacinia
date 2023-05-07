(ns service
  (:require [graphql.core :as g]
            [io.pedestal.http :as http]
            [io.pedestal.interceptor :as i] 
            [reitit.http :as reitit-http]
            [muuntaja.core :as m]
            [reitit.http.interceptors.muuntaja :as im]
            [reitit.http.interceptors.parameters :as ip]
            [reitit.pedestal :as reitit]))

(def default-interceptor
  (i/interceptor
   {:name ::default
    :enter (fn [ctx]
             (assoc ctx :response {:status 200
                                   :body   "hello, world. I did it!"}))}))

(defn default []
  ["/" {:get {:handler (fn [_] {:status 200
                                :body   "Hello World"})}}])

(defn routes [config]
  [(default)
   (g/routes config)])


(defn service-map [{:keys [db]
                    :as   config}] 
  (-> {:io.pedestal.http/type   :jetty
       :io.pedestal.http/port   3024
       :io.pedestal.http/join?  false
       :io.pedestal.http/routes []}
      (http/default-interceptors)
      (reitit/replace-last-interceptor
       (reitit/routing-interceptor
        (reitit-http/router
         (routes db)
         {:data {:muuntaja     m/instance
                 :interceptors [(im/format-interceptor)
                                (ip/parameters-interceptor)]}})))
                              

                            
          
            
      (http/dev-interceptors)))

(comment
  :rcf)

