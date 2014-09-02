(ns galah.handler
  (:require [lamina.core :refer :all]
            [aleph.http :refer :all]
            [clojure.data.json :as json]))

(def broadcast-channel (permanent-channel))

(defn parse-message [messageJson]
  (str ((json/read-str messageJson) "message")))

(defn respond [name message]
  (str name ": " (parse-message message)))

(defn chat-handler [ch handshake]
  (receive ch
    (fn [input]
      (let [name ((json/read-str input) "name")]
        (siphon (map* (partial respond name) ch) broadcast-channel)
        (siphon broadcast-channel ch)))))

(defn -main []
  (start-http-server chat-handler {:port 8008 :websocket true})
  (println "server starting"))
