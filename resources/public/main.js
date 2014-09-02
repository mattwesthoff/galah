(function() {
  $(function() {
    var wsConnection;
    wsConnection = new WebSocket("http:localhost:8008");
    wsConnection.onopen(function() {
      return console.log("opened");
    });
    wsConnection.onclose(function() {
      return console.log("closed");
    });
    wsConnection.onerror(function(error) {
      return console.log("error: " + error);
    });
    wsConnection.send("have a string!");
    return wsConnection.onmessage(function(e) {
      var message;
      message = e.data;
      return $("#messageArea").text(message);
    });
  });

}).call(this);
