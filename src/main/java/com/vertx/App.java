package com.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.reactivex.core.Vertx;

public class App {
    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        String[] stringArray = {"1", "2", "3"};

        vertx.rxDeployVerticle(SocketServer.class.getName())
                .subscribe(res ->
                        vertx.rxDeployVerticle(SocketClient.class.getName(), new DeploymentOptions().setWorker(true))
                                .subscribe(res1 -> {
                                    for (String string : stringArray) {
                                        vertx.eventBus()
                                                .<String>rxSend("/socketClient", string)
                                                .toFlowable()
                                                .subscribe(
                                                        res4 -> System.out.println("Response From Socket " + res4.body() + " For the Request " + string),
                                                        throwable -> {
                                                            System.out.println("Error from socket for the string " + string);
                                                            throwable.printStackTrace();
                                                        });
//                                        Thread.sleep(30);
                                    }
                                }, Throwable::printStackTrace));

        vertx.rxDeployVerticle(HTTPServer.class.getName())
                .subscribe(res2 ->
                        vertx.rxDeployVerticle(HTTPClient.class.getName())
                                .subscribe(res3 -> {
                                    for (String string : stringArray) {
                                        vertx.eventBus()
                                                .<String>rxSend("/httpClient", string)
                                                .subscribe(res5 -> System.out.println("Response From HTTP " + res5.body() + " For the Request " + string), err -> {
                                                });
                                    }
                                }));
    }
}
