package lab01;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.*;


class MyAgent extends AbstractVerticle {
	
	private String actionResultURI;
	
	public MyAgent() {
	}

	public void start() {
		WebClient client = WebClient.create(this.vertx);

		JsonObject msg = new JsonObject();
		msg.put("fade", new JsonObject()
				.put("input", new JsonObject()
					.put("duration", 1000)
					.put("brightness", 50)));
				
		Promise<HttpResponse<Buffer>> promiseActReq = Promise.promise();

		log("Requesting action fade...");

		client
		  .post(8888, "localhost", "/actions/fade")
		  .sendJson(msg, promiseActReq);
		
		Future futActReq = promiseActReq.future().onComplete(ar -> {
		    if (ar.succeeded()) {
			      HttpResponse<Buffer> response = ar.result();

			      log("Received response with status code " + response.statusCode());
			      log(response.bodyAsJsonObject().encodePrettily());

			      JsonObject reply = response.bodyAsJsonObject();
			      actionResultURI = reply.getJsonObject("fade").getString("href");
			    } else {
			      log("Something went wrong " + ar.cause().getMessage());
			    }
			  });
		
		Promise<HttpResponse<Buffer>> actionResPromise = Promise.promise();

		futActReq.onComplete(ar -> {
		      log("Action Result URI: " + actionResultURI);
		      log("Retrieving Action status...");
			  client
				  .get(8888, "localhost", actionResultURI)
				  .send(actionResPromise);
		});
		
		Future futActResult = actionResPromise.future().onComplete(ar -> {
			HttpResponse<Buffer> response = ar.result();
		    if (ar.succeeded()) {
		    	HttpResponse<Buffer> res = ar.result();

			    log("Received response with status code " + res.statusCode());
			    log("\n" + response.bodyAsJsonObject().encodePrettily());

			    
			    System.exit(0);
			} else {
			    log("Something went wrong " + ar.cause().getMessage());
			}
		});

		
	}
	
	private void log(String msg) {
		System.out.println("[MyAgent] " + msg);
	}
}


public class ExecAction {
	static public void main(String[] args) {	
		Vertx vertx = Vertx.vertx();		
		vertx.deployVerticle(new MyAgent());
	}

}
