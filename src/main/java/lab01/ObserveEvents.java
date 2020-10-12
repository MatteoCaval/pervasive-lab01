package lab01;

import io.vertx.core.Vertx;

public class ObserveEvents {

    static public void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MyObserverAgent());
    }

}
