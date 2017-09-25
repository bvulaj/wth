package com.redhat.it.wth;

import com.redhat.it.wth.handler.RepoHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerVerticle extends AbstractVerticle {

	@Value("${http.port:8080}")
	private int port;

	@Autowired
	private RepoHandler repoHandler;

	@Override
	public void start() throws Exception {
		vertx.createHttpServer().requestHandler(router()::accept).listen(port);
	}

	private Router router() {
		Router router = Router.router(vertx);
		router.route("/hello").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("Content-Type", "application/json");
			response.end(Json.encodePrettily("Hello World"));
		});
		repoHandler.setVertx(vertx);
		router.route("/repos").handler(repoHandler);
		router.route().handler(StaticHandler.create());
		return router;
	}
}