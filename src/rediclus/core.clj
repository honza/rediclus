(ns rediclus.core
  (:require [lamina.core :refer :all]
            [aleph.tcp :refer :all]
            [gloss.core :refer :all])
  (:require [clojure.string :as s])
  (:gen-class))

(def crlf "\r\n")
(def crlf-regex #"\r\n")
(def ok (str "+OK" crlf))
(def db (atom {}))

(defn format-value [v]
  (apply str "$" (count v) crlf v crlf))

(defn format-error [m]
  (str "-ERR " m crlf))

(defn db-get [k]
  (@db k))

(defn db-set [k v]
  (swap! db assoc k v))

(defn parse-command [c]
  (let [args (s/split c crlf-regex)
        num-args (read-string (s/join "" (rest (first args))))
        valid (= (* 2 num-args) (dec (count args)))
        pairs (partition 2 (nthrest args 3))]
    (if valid
      {:command (keyword (nth args 2))
       :args (map second pairs)}
      {:command :fail})))

(defn handle [s]
  (let [command (parse-command s)]
    (condp = (:command command)
      :get  (format-value
              (db-get
                (first (:args command))))
      :set  (do
              (apply db-set (:args command))
              ok)
      :fail (format-error "malformed command")
      nil)))


(defn handler [ch client-info]
  (receive-all ch
               #(enqueue ch 
                         (handle %))))

(defn start [port]
  (start-tcp-server handler {:port port,
                             :frame (string :utf-8)}))

(defn -main [& args]
  (let [port (or (first args) 6379)]
    (start port)
    (println "Server started on port" port)))
