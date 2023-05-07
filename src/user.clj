(ns user
  (:require [integrant.core :as ig]
            [integrant.repl :as ig-repl]))

(ig-repl/set-prep!
 (fn [] (-> "resources/config.edn"
            (slurp)
            (ig/read-string)
            (ig/prep))))

(def go ig-repl/go)
(def reset ig-repl/reset)
(def halt ig-repl/halt)

(comment
  :rcf)

