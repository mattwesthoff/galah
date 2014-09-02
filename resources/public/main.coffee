$ ->
  wsConnection = new WebSocket("http:localhost:8008")
  wsConnection.onopen ->
    console.log("opened")

  wsConnection.onclose ->
    console.log("closed")

  wsConnection.onerror (error) ->
    console.log("error: " + error)

  wsConnection.send("have a string!");

  wsConnection.onmessage (e) ->
    message = e.data
    $("#messageArea").text(message)
