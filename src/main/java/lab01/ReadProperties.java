package lab01;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.*;


public class ReadProperties {

	static public void main(String[] args) {
	
		Vertx vertx = Vertx.vertx();		
		WebClient client = WebClient.create(vertx);

		/* reading the list of properties */
		
		client
		  .get(8888, "localhost", "/properties")
		  .send(ar -> {
		    if (ar.succeeded()) {
		      HttpResponse<Buffer> response = ar.result();
		      System.out.println("Received response with status code " + response.statusCode());
		      
		      JsonObject td = response.bodyAsJsonObject();
		      synchronized (System.out) {
		    	  System.out.println("Properties: \n" + td.encodePrettily());
		      }
		      
		    } else {
		      System.out.println("Something went wrong " + ar.cause().getMessage());
		    }
		  });

		/* reading a specific property */
		
		client
		  .get(8888, "localhost", "/properties/brightness")
		  .send(ar -> {
		    if (ar.succeeded()) {
		      // Obtain response
		      HttpResponse<Buffer> response = ar.result();
		      synchronized (System.out) {
			      System.out.println("Received response with status code " + response.statusCode());
			      
			      JsonObject td = response.bodyAsJsonObject();
			      System.out.println("Property: \n" + td.encodePrettily());
		      }		      
		    } else {
		      System.out.println("Something went wrong " + ar.cause().getMessage());
		    }
		  });
		
		
	}

}
