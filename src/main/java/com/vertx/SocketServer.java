package com.vertx;


import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;

public class SocketServer extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) {
        this.vertx.createHttpServer()
                .websocketHandler(serverWebSocket -> {
                    System.out.println(serverWebSocket.remoteAddress());
                    System.out.println(serverWebSocket.hashCode());
                    if ("/socketDuplicate".equals(serverWebSocket.uri())) {
                        serverWebSocket.handler(buffer -> {
                            System.out.println("From Socket Server " + buffer.toString());
                            serverWebSocket.writeTextMessage(buffer.toString() + " " + buffer.toString());
                        });
                    }
                })
                .rxListen(9090)
                .toFlowable()
                .subscribe(onSuccess -> {
                    System.out.println("Socket Server Verticle Deployed");
                    startFuture.complete();
                }, error -> startFuture.fail("Error while starting Socket Server"));
    }
}
