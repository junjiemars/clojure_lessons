(ns oracle.core
  (:require [clojure.tools.logging :as log])
  (:import (oracle.jdbc.driver OracleDriver)
           (oracle.jdbc.pool OracleDataSource)
           (java.sql Driver
                     DriverManager
                     Connection)
           (java.util Properties)))

(defn register-driver [^OracleDriver d]
  (when d
    (DriverManager/registerDriver d)))

(defn deregister-driver [^OracleDriver d]
  (when d
    (DriverManager/deregisterDriver d)))

(defn open-datasource
  ([{:keys [url user password] :as spec}]
   (open-datasource spec {:cached? false :min-limit 1 :max-limit 4}))
  ([{:keys [url user password] :as spec}
    {:keys [cached? min-limit max-limit] :as options}]
   (let [{:keys [cached? min-limit max-limit]} options
         ds (OracleDataSource.)]
     (.setURL ds url)
     (.setUser ds user)
     (.setPassword ds password)
     (when cached?
       (let [p (Properties.)]
         (.setProperty p "MinLimit" (str min-limit))
         (.setProperty p "MaxLimit" (str max-limit))
         (.setConnectionCachingEnabled ds true)
         (.setConnectionCacheProperties ds p)))
     ds)))

(defn close-datasource [^OracleDataSource ds]
  (when ds
    (.close ds)))

(defn open-connection [^OracleDataSource ds]
  (when ds
    (.getConnection ds)))

(defn close-connect [^Connection c]
  (when c
    (.close c)))





