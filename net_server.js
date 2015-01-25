var vertx = require('vertx');
var console = require('vertx/console');
var container = require("vertx/container");
var logger = container.logger;
var bus = vertx.eventBus;

var now = function() {
    var myDate = new Date();
    return myDate.toString();
};

// ----------------------------------------------------

var VERTICLE_ID = "ANYLINK.NET.SERVER";

var SERVER_IP = 'localhost';
var SERVER_PORT = 9002;

// ----------------------------------------------------

var server = vertx.createNetServer();

server.connectHandler(function(sock) {
    
    sock.dataHandler(function(buffer) {
        logger.info(now() + " - "+VERTICLE_ID+" > NET request received > " + buffer.toString());

        // should send the request to ANYLINK.PROCESSOR via bus
        bus.send('ANYLINK.PROCESSOR', data, function(reply) {
            logger.info(now() + " - "+VERTICLE_ID+" > Got a reply from ANYLINK.PROCESSOR > " + reply);
            var buff = new vertx.Buffer(reply);
            sock.write(buff);
        });
    });

    sock.closeHandler(function() {});

    sock.exceptionHandler(function(ex) {});

});

server.listen(SERVER_PORT, SERVER_IP, function(err) {
    if (!err) {
        logger.info(now() + " - "+VERTICLE_ID+" > listening on " + SERVER_IP + ":" + SERVER_PORT);
    }
});
