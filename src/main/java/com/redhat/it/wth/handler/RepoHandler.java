
package com.redhat.it.wth.handler;

import com.redhat.it.wth.model.Repo;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.net.URI;
import java.net.URISyntaxException;

public class RepoHandler implements Handler<RoutingContext> {

	@Override
	public void handle(final RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.putHeader("Content-Type", "application/json");
		try {
			// TODO move this to read properties
			response.end(Json.encodePrettily(new Repo("acstools", new URI("ssh://gitolite.corp.redhat.com/puppet-cfg/modules/acstools.git"))));
		} catch (URISyntaxException e) {
			// TODO real error handling
			routingContext.response().setStatusCode(500);
			routingContext.response().setStatusMessage("Internal Server Error");
			response.end();
		}

	}
}