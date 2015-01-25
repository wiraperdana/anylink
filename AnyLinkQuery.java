import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;


public class AnyLinkQuery extends Verticle {

    final static String VERTICLE_ID = "ANYLINK.QUERY";

    public void start() {
        // send message to ANYLINK.QUERY channel with this format 
        // { query: "query_name", params: { param1: "", param2: "" } }
        vertx.eventBus().registerHandler(VERTICLE_ID, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> msg) {
                System.out.println(now() + " - "+VERTICLE_ID+" > Received QUERY:" + msg.body().getField("query"));
                
                // if(msg.body().getField("query").equals("cekServerTime")) {
                //     int result = cekServerTime(msg.body().getField("params").getField("sLOG"));
                //     msg.reply(result);
                // }
            }
        });
    }

    // Sample shared Query
    public int cekServerTime(StringBuilder sLOG) {
        String sTimestamp = "";
        String sDelay = "";
        String sSql = "";
        String sLog = "";
        sLOG.setLength(0);
        try {
            // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // Date date = new Date();
            // sTimestamp = dateFormat.format(date);
            // sSql = String.format("select time_to_sec(timediff(current_timestamp, '%s')) as delay", sTimestamp);

            // Message msg = this.query(sSql);

            // System.out.println(now() + " - "+VERTICLE_ID+" > Result of query:" + msg.body());

            // preparedStatement = dbcon.prepareStatement(sSql);
            // resultSet = preparedStatement.executeQuery();

            // if (resultSet.next()) {
            //     sDelay = resultSet.getString("delay");
            //     sLog = String.format("cekServerTime delay [%s]", sDelay);
            //     sLOG.append(sLog);
            //     return Integer.parseInt(sDelay);
            // }

        } catch (Exception e) {
            e.printStackTrace();
            //throw e;
        } finally {

        }
        return 0;
    }

    // protected Message query(String sql) {
    //     vertx.eventBus().send("ANYLINK.MYSQL.DAO", sql, new Handler<Message>() {
    //         @Override
    //         public void handle(Message dbMsg) {
    //             // System.out.println(now() + " - "+VERTICLE_ID+" > Result of query:" + dbMsg.body());
    //             return new Message();
    //         }
    //     });
    // }

    // protected Message redis(JsonObject commands) {
    //     vertx.eventBus().send("ANYLINK.REDIS.DAO", commands, new Handler<Message>() {
    //         @Override
    //         public void handle(Message dbMsg) {
    //             // if (dbMsg.body().status.equals("ok")) {
    //             //     // do something with dbMsg
    //             //     System.out.println(now() + " - "+VERTICLE_ID+" > Result of query:" + dbMsg.body());
    //                 return new Message();
    //             // } else {
    //             //     System.out.println(now() + " - "+VERTICLE_ID+" > Error: " + dbMsg);
    //             // }
    //         }
    //     });
    // }

    protected String now() {
        // Sat Dec 20 2014 15:51:05 GMT+0700 (WIB)
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT'Z (z)");
        Date date = new Date();
        return dateFormat.format(date);
    }
}