package lab01;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import io.vertx.core.json.JsonObject;

public class MyObserverAgent extends AbstractVerticle {
	
	public MyObserverAgent() {
	}

	public void start() {
		HttpClient cli = vertx.createHttpClient();

		WebSocketConnectOptions opt = new WebSocketConnectOptions();
		opt.addSubProtocol("webthing");
		opt.setHost("localhost");
		opt.setPort(8888);
		opt.setURI("/");
		
		log ("Connecting to the thing...");
		
		cli.webSocket(opt, res -> {
			if (res.succeeded()) {
				log("Connected!");
				log("Subscribing to all events, including overheated");

				WebSocket ws = res.result();				
				JsonObject msg = new JsonObject();
				msg
				.put("messageType", "addEventSubscription")
				.put("data", new JsonObject()
					.put("overheated", new JsonObject()));
				
				ws.writeTextMessage(msg.encodePrettily());

				/* handling events */
				
				ws.handler(data -> {
					log("new event observed: \n" + data.toJsonObject().encodePrettily());
				});
				

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