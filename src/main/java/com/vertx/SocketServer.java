package com.vertx;


import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;

public class SocketServer extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) {
        this.vertx.createHttpServer()
                .websocketHandler(serverWebSocket -> {
                    if ("/socketDuplicate".equals(serverWebSocket.uri())) {
                        serverWebSocket.handler(buffer -> serverWebSocket.writeTextMessage(buffer.toString() + " " + buffer.toString()));
                    }
                })
                .rxListen(9090)
                .toFlowable()
                .subscribe(onSuccess -> startFuture.complete(), error -> startFuture.fail("Error while starting Socket Server"));
    }
}
