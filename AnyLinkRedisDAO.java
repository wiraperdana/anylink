import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/** Register a verticle with the event bus, send events and executes queries to the REDIS driver */
public class AnyLinkRedisDAO extends Verticle {

    final String VERTICLE_ID = "ANYLINK.REDIS.DAO";
    final String DB_ID = "ANYLINK.REDIS.DB";
    
    public void start() {
        JsonObject config = new JsonObject();
        config.putString("address", DB_ID);
        config.putString("host", "116.90.165.11");
        config.putNumber("port", 6379);

        container.deployModule("io.vertx~mod-redis~1.1.4", config, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                if (event.failed()) {
                    System.out.println(now() + " - "+VERTICLE_ID+" > failed: " + event.failed() + " cause: " + event.cause());
                    container.exit();
                }

                System.out.println(now() + " - "+VERTICLE_ID+" > successfully deployed: " + event.toString());

                vertx.eventBus().registerHandler(VERTICLE_ID, new Handler<Message<JsonObject>>() {
                    @Override
                    public void handle(final Message<JsonObject> received) {
                        // System.out.println(now() + " - "+VERTICLE_ID+" > Received QUERY:" + received.body.getField("value"));
                        // JsonObject value = received.body.getField("value");
                        // // the value is a JSON doc with the following properties
                        // // channel - The channel to which this message was sent
                        // // pattern - Pattern is present if you use psubscribe command and is the pattern that matched this message channel
                        // // message - The message payload



                        // vertx.eventBus().send(DB_ID, value, new Handler<Message<JsonObject>>() {
                        //     @Override   public void handle(Message<JsonObject> dbMsg) {
                        //         // JsonObject result = dbMsg.body();
                        //         // System.out.println(result.toString());
                        //         // msg.reply(result.getArray("results").<JsonArray>get(0).get(0).toString());
                        //         msg.reply(dbMsg);
                        //     }
                        // });
                    }
                });
            }
        });
    }

    protected String now() {
        // Sat Dec 20 2014 15:51:05 GMT+0700 (WIB)
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT'Z (z)");
        Date date = new Date();
        return dateFormat.format(date);
    }
/*
    protected void sendEvent() {
        vertx.eventBus().send("query.me", "select '' || count(*) from pg_tables;", new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                System.out.println("Result of query:" + event.body());
                count++;
                if (count == expected) {
                    System.out.println("Test complete");
                    System.out.println("Duration:" + (System.currentTimeMillis() - start));
                }
                System.out.println("Count: " + count);
            }
        });
    }
*/
}