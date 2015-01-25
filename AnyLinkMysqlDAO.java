import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/** Register a verticle with the event bus, send events and executes queries to the PostgresDB driver */
public class AnyLinkMysqlDAO extends Verticle {

    final String VERTICLE_ID = "ANYLINK.MYSQL.DAO";
    final String DB_ID = "ANYLINK.MYSQL.DB";
    
    public void start() {
        JsonObject config = new JsonObject();
        config.putString("address", DB_ID);
        config.putString("connection", "MySQL");
        config.putString("host", "localhost");
        config.putNumber("port", 3306);
        config.putString("username", "edx");
        config.putString("password" , "edx123mysql");
        config.putString("database", "mshdatabase_dev");

        container.deployModule("io.vertx~mod-mysql-postgresql_2.11~0.3.1", config, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> event) {
                if (event.failed()) {
                    System.out.println(now() + " - "+VERTICLE_ID+" deploy failed: " + event.failed() + " cause: " + event.cause());
                    container.exit();
                }

                System.out.println(now() + " - "+VERTICLE_ID+" successfully deployed: " + event.toString());

                vertx.eventBus().registerHandler(VERTICLE_ID, new Handler<Message<String>>() {
                    @Override
                    public void handle(final Message<String> msg) {
                        System.out.println(now() + " - "+VERTICLE_ID+" > Received QUERY:" + msg.body());
                        JsonObject json = new JsonObject();
                        json.putString("action", "raw");
                        json.putString("command", msg.body());
                        vertx.eventBus().send(DB_ID, json, new Handler<Message<JsonObject>>() {
                            @Override   public void handle(Message<JsonObject> dbMsg) {
                                // JsonObject result = dbMsg.body();
                                // System.out.println(result.toString());
                                // msg.reply(result.getArray("results").<JsonArray>get(0).get(0).toString());
                                msg.reply(dbMsg);
                            }
                        });
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