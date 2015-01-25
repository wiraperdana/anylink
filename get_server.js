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

var VERTICLE_ID = "ANYLINK.GET.SERVER";

var SERVER_IP = 'localhost';
var SERVER_PORT = 9001;

// ----------------------------------------------------

var server = http.createHttpServer();
var routeMatcher = new http.RouteMatcher();

routeMatcher.get('/api/anylink/get', function(req) {

    req.bodyHandler(function(data) {
        // var postData = JSON.parse(data);
        // logger.info(now() + " - ANYLINK.GET.SERVER > HTTP GET request received > " + JSON.stringify(postData, undefined, 2));

        logger.info(now() + " - "+VERTICLE_ID+" > HTTP GET request received > " + req.params().get('param'));

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
