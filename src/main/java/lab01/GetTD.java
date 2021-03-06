package lab01;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.json.*;

/**
 * come ottenere la thing description
 */
public class GetTD {

    static public void main(String[] args) {

        // vertx per usare http in modo asincrono
        Vertx vertx = Vertx.vertx();
        // altra libreria che permette di facilitare interazione
        WebClient client = WebClient.create(vertx);

        client.get(8888, "localhost", "/")
                .send(ar -> {
                    if (ar.succeeded()) {
                        // Obtain response
                        HttpResponse<Buffer> response = ar.result();
                        System.out.println("Received response with status code " + response.statusCode());

                        JsonObject td = response.bodyAsJsonObject();
                        System.out.println("Thing Description: \n" + td.encodePrettily());

                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                    System.exit(0);
                });


    }

}
