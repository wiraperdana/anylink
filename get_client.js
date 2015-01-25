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

var VERTICLE_ID = "ANYLINK.GET.CLIENT";

var HOST_IP = 'localhost';
var HOST_PORT = 9091;

// ----------------------------------------------------

var client = vertx.createHttpClient().host(HOST_IP).port(HOST_PORT);

// ANYLINK.PROCESSOR -> ANYLINK.CLIENT -> HOST
// should receive message from ANYLINK.PROCESSOR via bus then send it to HOST
bus.registerHandler(VERTICLE_ID, function(message, replier) {

    logger.info(now() + ' - '+VERTICLE_ID+' | Sending ' + message.length() + ' bytes of data to HOST');

    // HOST -> ANYLINK.CLIENT -> ANYLINK.PROCESSOR
    // should receive a reply message from HOST then reply the message to ANYLINK.PROCESSOR
    client.get("/some-uri/", function(resp) {

      resp.bodyHandler(function(body) {
        logger.info(now() + ' - '+VERTICLE_ID+' | Received ' + body.length() + ' bytes of data from HOST');

        // var sql = "select * from par_isocfg where kode_inst='000555' and mti='0100'";
        // bus.send("anylink.dao", sql, function(reply) {
        //     replier(buffer.toString() + "\n\n" + reply.status);
        // });
      });

    });
});
});
