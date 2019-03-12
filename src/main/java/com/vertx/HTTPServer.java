package com.vertx;

import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;


public class HTTPServer extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);

        router.get("/sanity").handler(rc -> rc.response().end("UP"));

        router.get("/duplicateHTTP").handler(
                rc -> rc.request().bodyHandler(
                        buffer -> {
                            rc.response()
                                    .putHeader("Content-Type", "text/plain")
                                    .setStatusCode(200)
                                    .end(buffer.toString() + " " + buffer.toString());
                        }
                ));

        this.vertx.createHttpServer()
                .requestHandler(router)
                .rxListen(8080)
                .toFlowable()
                .subscribe(res -> {
                            System.out.println(HTTPServer.class.getName() + " deployed");
                            startFuture.complete();
                        },
                        err -> {
                            startFuture.fail("HTTP Server Verticle failed to deploy");
                            err.printStackTrace();
                        });
    }
}
