package com.redhat.it.wth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

@Component
public class ServerVerticle extends AbstractVerticle {

	@Value("${http.port}")
	private int port;

	@Override
	public void start() throws Exception {
		vertx.createHttpServer().requestHandler(router()::accept).listen(port); // port
	}

	private Router router() {
		Router router = Router.router(vertx);
		router.route().handler(StaticHandler.create());

		router.route("/hello").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("Content-Type", "application/json");
			response.end(Json.encodePrettily("Hello World"));
		});
		return router;
	}
}