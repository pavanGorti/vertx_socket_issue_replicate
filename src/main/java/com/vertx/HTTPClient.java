package com.vertx;

import io.vertx.core.Future;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.web.client.WebClient;

public class HTTPClient extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) {
        WebClient webClient = WebClient.create(vertx, new WebClientOptions().setKeepAlive(true));
        this.vertx.eventBus()
                .<String>consumer("/httpClient")
                .toFlowable()
                .subscribe(message -> hitServer(message, webClient));
        startFuture.complete();
    }

    private void hitServer(Message<String> message, WebClient webClient) {
        webClient.getAbs("http://localhost:8080/duplicateHTTP")
                .rxSendBuffer(Buffer.buffer(message.body()))
                .toFlowable()
                .subscribe(res -> message.reply(res.body().toString()), err -> {
                });
    }
}
