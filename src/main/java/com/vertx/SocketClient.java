package com.vertx;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpClient;

public class SocketClient extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {

        HttpClient client = vertx.createHttpClient();
        client.websocket(9090, "localhost", "/socketDuplicate", webSocket -> {
            this.vertx.eventBus().<String>consumer("/socketClient")
                    .toFlowable()
                    .subscribe(message ->
                            webSocket.writeBinaryMessage(Buffer.buffer(message.body())).handler(buffer -> {
                                System.out.println("Buffer is " + buffer.toString() + " for request " + message.body());
                                message.reply(buffer.toString());
                            }));
            startFuture.complete();
            System.out.println("Socket Client Verticle Deployed");
        });
    }
}
