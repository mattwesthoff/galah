(ns galah.test.handler
  (:require [clojure.test :refer :all]
            [galah.handler :refer :all]
            [lamina.core :refer :all]
            [aleph.http :refer :all]))

(defmacro with-server [server & body]
  `(let [kill-fn# ~server]
     (try
       ~@body
       (finally
	 (kill-fn#)))))

(defmacro with-handler [handler & body]
  `(with-server (start-http-server ~handler
		  {:port 8007
                   :websocket true
		   })
     ~@body))
     
(defn ws-client []
  (websocket-client {:url "ws://localhost:8007"}))

(deftest test-chat-handler
  (with-handler chat-handler
    (let [ch (wait-for-result (ws-client) 1000)]
      (enqueue ch "b")
      (dotimes [_ 100]
        (enqueue ch "a")
        (is (= "b: a" (wait-for-result (read-channel ch) 500))))
      (close ch))))
