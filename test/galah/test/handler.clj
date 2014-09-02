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

; (defmacro with-rejecting-handler [handler & body]
;   `(with-server (start-http-server ~handler
; 		  {:port 8007
;                    :websocket true
;                    :websocket-handshake-handler (fn [ch# _#] (enqueue ch# {:status 404}))
; 		   })
;      ~@body))

(defn echo-handler [ch req]
  (siphon ch ch))

(defn ws-client []
  (websocket-client {:url "ws://localhost:8007"}))

(deftest test-echo-handler
  (with-handler echo-handler
    (let [ch (wait-for-result (ws-client) 1000)]
      (dotimes [_ 100]
        (enqueue ch "a")
        (is (= "a" (wait-for-result (read-channel ch) 500))))
      (close ch))))

; (deftest test-handshake-handler
;   (with-rejecting-handler echo-handler
;     (is (thrown? Exception (wait-for-result (ws-client) 1000)))))
;

(deftest test-app
  (testing "main route"
    (is true)))
