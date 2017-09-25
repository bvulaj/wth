
package com.redhat.it.wth.handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.it.wth.model.Repo;
import com.redhat.it.wth.scanner.repo.RepoScanner;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

@Component
public class RepoHandler implements Handler<RoutingContext>, NeedsVertx<RepoHandler> {

	@Value("${scan.repos}")
	private List<String> repos; // TODO: use this

	private Vertx vertx;

	@Autowired
	List<RepoScanner> repoScanners;

	@Override
	public void handle(final RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.putHeader("Content-Type", "application/json");

		try {
			// pass vertx if we need it
			repoScanners.stream()
					.filter(scanner -> scanner instanceof NeedsVertx)
					.map(scanner -> (NeedsVertx<?>)scanner)
					.forEach(needsVertxScanner -> needsVertxScanner.setVertx(vertx));

			final URL repoUrl = new URL("https://gitolite.corp.redhat.com/cgit/it-smw");
			final Set<Repo> repoSet = repoScanners.stream()
					.flatMap(scanner -> scanner.scanForRepos(repoUrl).stream())
					.collect(Collectors.toSet());
			response.end(Json.encodePrettily(repoSet));
		} catch (MalformedURLException e) {
			// TODO real error handling
			routingContext.response().setStatusCode(500);
			routingContext.response().setStatusMessage("Internal Server Error");
			response.end();
		}

	}

	@Override
	public RepoHandler setVertx(final Vertx vertx) {
		this.vertx = vertx;
		return this;
	}
}