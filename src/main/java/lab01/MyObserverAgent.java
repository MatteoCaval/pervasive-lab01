package lab01;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.core.json.JsonObject;

/**
 * mi evita ad esempio di fare pooling per capire che un'azione è stata completata
 */
public class MyObserverAgent extends AbstractVerticle {

    public MyObserverAgent() {
    }

    public void start() {
        HttpClient cli = vertx.createHttpClient();

        // websocket, in vertx gestite dal httpclient (in questa versione almeno)
        WebSocketConnectOptions opt = new WebSocketConnectOptions();
        opt.addSubProtocol("webthing");
        opt.setHost("localhost");
        opt.setPort(8888);
        opt.setURI("/"); // perche uso quella singola

        log("Connecting to the thing...");

        cli.webSocket(opt, res -> {
            if (res.succeeded()) {
                log("Connected!");
                log("Subscribing to all events, including overheated");

                WebSocket ws = res.result();
                JsonObject msg = new JsonObject();
                msg
                        .put("messageType", "addEventSubscription") // messaggio predefinito
                        .put("data", new JsonObject()
                                .put("overheated", new JsonObject())); // filtro che voglio osservare riceverò gli eventi legati ad action e al cambio di stato delle proprietà

                ws.writeTextMessage(msg.encodePrettily()); // mando un messaggio specificando che voglio fare una subsription

                /* handling events */
                // sono in event loop, quindi handler lo posso anche mettere dopo
                ws.handler(data -> log("new event observed: \n" + data.toJsonObject().encodePrettily()));


            } else {
                log("Not Connected!");
                System.exit(0);
            }
        });
    }

    private void log(String msg) {
        System.out.println("[MyAgent] " + msg);
    }
}