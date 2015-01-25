import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/** Register a verticle with the event bus, send events and executes queries to the PostgresDB driver */
public class AnyLinkProcessor extends Verticle {

    final String VERTICLE_ID = "ANYLINK.PROCESSOR";

    public void start() {
        System.out.println(now() + " - "+VERTICLE_ID+" ready!");

        // should receive request (JSON) from server to be proceed and then forward it (ISO) to ocms.client bus
        vertx.eventBus().registerHandler(VERTICLE_ID, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> reqMsg) {

                String anyReqMsg = buildMessage(reqMsg);
                System.out.println(now() + " - "+VERTICLE_ID+" > Sending request to ANYLINK.[NET|POST|GET].CLIENT >" + anyReqMsg);
                vertx.eventBus().send("ANYLINK.[NET|POST|GET].CLIENT", anyReqMsg, new Handler<Message<String>>() {
                    // receive response from ocms.client
                    @Override
                    public void handle(Message<String> ocmsMsg) {
                        System.out.println(now() + " - "+VERTICLE_ID+" > Parsing ANYLINK.[NET|HTTP].CLIENT response >" + ocmsMsg.body());
                        String ocmsRespMsg = parseMessage(ocmsMsg);
                        reqMsg.reply(ocmsRespMsg);
                    }
                });
            }
        });

    }

    protected String buildMessage(Message<JsonObject> msg) {
        JsonObject jsonReq = msg.body();

        // var testData = "02003601202238400000C00802000000141215084009000771084009141215B000000050A00000005763768611700410630102000000001370004900000064012C48FC062400C95EF6E0B6AE407E4CD62FC85584275EC8266278E5C69F5EAF879316EFB8AE77D185D7F4278B3F2A369800000000000000000000000000000000000000001F6803";
        String testData = "02003601002238400000C00802000000151114250600000157250600151114B000000002A00000000463664193500011758000000000001110004900001388FF82966D279261E5859E917E7DC53592135D889B75724FCFEF240981FC8ECC120A3B61402BE15BCC66FA44D4880B878EEE0000000000000000000000000000000000000000FA9C03";

        // return jsonReq.encodePrettily();
        return testData;
    }

    protected String parseMessage(Message<String> msg) {
        String sDetail = "Something HERE...";
        String testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\"><soapenv:Body><ns:OCMSTRXResponse xmlns:ns=\"http://services.samples\"><ns:return xmlns:ax21=\"http://services.samples/xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ax21:OCMSTRXResponse\">" + sDetail + "</ns:return></ns:OCMSTRXResponse></soapenv:Body></soapenv:Envelope>";

        return testData;
    }

    protected String now() {
        // Sat Dec 20 2014 15:51:05 GMT+0700 (WIB)
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT'Z (z)");
        Date date = new Date();
        return dateFormat.format(date);
    }
}