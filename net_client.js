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

var VERTICLE_ID = "ANYLINK.NET.CLIENT";

var HOST_IP = 'localhost';
var HOST_PORT = 9002;

// ----------------------------------------------------

var client = vertx.createNetClient();

client.connect(HOST_PORT, HOST_IP, function(err, sock) {
    if (!err) {
        logger.info(now() + " - "+VERTICLE_ID+" connected!");

        // ANYLINK.PROCESSOR -> ANYLINK.CLIENT -> HOST
        // should receive message from ANYLINK.PROCESSOR via bus then send it to HOST
        bus.registerHandler(VERTICLE_ID, function(message, replier) {
            var buff = new vertx.Buffer(message);
            sock.write(buff);
            logger.info(now() + ' - '+VERTICLE_ID+' > Sending ' + buff.length() + ' bytes of data to HOST');
    
            // HOST -> ANYLINK.CLIENT -> ANYLINK.PROCESSOR
            // should receive a reply message from HOST then reply the message to ANYLINK.PROCESSOR
            sock.dataHandler(function(buffer) {
                logger.info(now() + ' - '+VERTICLE_ID+' > Received ' + buffer.length() + ' bytes of data from HOST');
                replier(buffer.toString());

                // var sql = "select * from par_isocfg where kode_inst='000555' and mti='0100'";
                // bus.send("anylink.dao", sql, function(reply) {
                //     replier(buffer.toString() + "\n\n" + reply.status);
                // });
            });
        });

        sock.endHandler(function() {
            logger.info(now() + ' - '+VERTICLE_ID+' > no more data to read');
        });

        sock.closeHandler(function() {
            logger.info(now() + ' - '+VERTICLE_ID+' > disconnected!');
        });
    } else
        logger.info(now() + ' - '+VERTICLE_ID+' > connection error > ' + err);
});
