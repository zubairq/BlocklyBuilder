(ns webapp.framework.server.systemfns

  [:require [clojure.string :as str]]
  [:use [korma.db]]
  [:use [korma.core]]
  [:use [webapp-config.settings]]
  [:use [webapp.framework.server.encrypt]]
<<<<<<< HEAD
=======
  [:require [webapp.framework.server.neo4j-helper :as nh]]
>>>>>>> d7aefc69922ac88df13b2ee993a4ef7d5eb45877
  (:require [clojurewerkz.neocons.rest :as nr])
  (:require [clojurewerkz.neocons.rest.nodes :as nn])
  (:require [clojurewerkz.neocons.rest.relationships :as nrl])
  (:require [clojurewerkz.neocons.rest.cypher :as cy])
)


(defdb db (postgres {:db *database-name*
                     :host *database-server*
                     :user *database-user*
                     :password *database-password*}))




(defn !say-hello [params]
    {:text (str "System Hello " (:name params))}
)


(defn !sql [{coded-sql :sql params :params}]
  (do
    (let [sql             (decrypt coded-sql)
          lower           (.toLowerCase sql)
          ]
      (println "SQL from client: " coded-sql " -> " sql)
      (cond
       (.startsWith lower "select")  (do (println "SELECT") (exec-raw [sql params] :results))
       :else                         (do (println "INSERT") (exec-raw [sql params]) [])
   ; []
    ))
  )
)



(defn !neo4j [{coded-cypher :cypher params :params}]
  (do
    (let [cypher          (decrypt coded-cypher)
          lower           (.toLowerCase cypher)
          ]
      (println "Cypher from client: " coded-cypher " -> " cypher)
      (cy/tquery cypher params)
    ))
  )



(defn !count-all-neo4j-records-with-field [ {field-name :field-name} ]
      (cy/tquery (str "START x = node(*) WHERE HAS(x." field-name ") RETURN count(x)") {} )
)



(defn !get-all-neo4j-records-with-field [ {field-name :field-name} ]
      (cy/tquery (str "START x = node(*) WHERE HAS(x." field-name ") RETURN x,ID(x)") {} )
)

;(!get-all-neo4j-records-with-field {:field-name "type"})




<<<<<<< HEAD
=======
(defn !add-to-simple-point-layer   [{node :node layer-name :layer-name}]
  (nh/add-to-simple-layer (:name node) (:x node) (:y node) layer-name)
)




(comment
  !add-to-simple-point-layer {:node           {:name "Lib2" :x 0.1 :y 0.1}
                              :layer-name     "ore2"})




(defn !find-names-within-distance [{x :x y :y dist-km :dist-km layer-name :layer-name}]
  (nh/find-names-within-distance layer-name x y dist-km)
)



(defn !find-names-within-bounds [{
                                  min-x :min-x
                                  min-y :min-y
                                  max-x :max-x
                                  max-y :max-y
                                  layer-name :layer-name}]
  (nh/find-names-within-bounds layer-name min-x max-x min-y max-y)
)







(comment !find-names-within-bounds
 {:layer-name "ore2"
  :min-x 0.0 :max-x 1.1 :min-y 50.0 :max-y 51.5})



;(!find-names-within-distance {:layer-name "ore2" :x 0 :y 0 :dist-km 1000})


>>>>>>> d7aefc69922ac88df13b2ee993a4ef7d5eb45877

(comment !neo4j {
         :cypher   (encrypt "START x = node(11) RETURN x")
         :params   {}})

               ;:params   {:ids (map :id [bob])}}))
