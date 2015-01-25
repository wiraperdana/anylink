var vertx = require('vertx');
var console = require('vertx/console');
var container = require("vertx/container");
var http = require('vertx/http');
var logger = container.logger;
var bus = vertx.eventBus;

var now = function() {
    var myDate = new Date();
    return myDate.toString();
};

// ----------------------------------------------------

var VERTICLE_ID = "ANYLINK.POST.SERVER";

var SERVER_IP = 'localhost';
var SERVER_PORT = 9000;

// ----------------------------------------------------

var server = http.createHttpServer();
var routeMatcher = new http.RouteMatcher();

routeMatcher.post('/api/anylink/post', function(req) {

    req.bodyHandler(function(data) {
        // var postData = JSON.parse(data);
        // logger.info(now() + " - "+VERTICLE_ID+" > HTTP POST request received > " + JSON.stringify(postData, undefined, 2));

        logger.info(now() + " - "+VERTICLE_ID+" > HTTP POST request received > " + data);

        // should send the request to ANYLINK.PROCESSOR via bus
        bus.send('ANYLINK.PROCESSOR', data, function(reply) {
            logger.info(now() + " - "+VERTICLE_ID+" > Got a reply from ANYLINK.PROCESSOR > " + reply);
            req.response.end(reply);
        });
    });

})

server.requestHandler(routeMatcher);

server.listen(SERVER_PORT, SERVER_IP, function(err) {
    if (!err) {
        logger.info(now() + " - "+VERTICLE_ID+" > listening on " + SERVER_IP + ":" + SERVER_PORT);
    }
});
