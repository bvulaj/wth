
package com.redhat.it.wth.handler;

import com.redhat.it.wth.model.Repo;
import com.redhat.it.wth.scanner.repo.RepoScanner;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
					.map(scanner -> (NeedsVertx<?>) scanner)
					.forEach(needsVertxScanner -> needsVertxScanner.setVertx(vertx));

			final URL repoUrl = new URL("https://gitolite.corp.redhat.com/cgit/it-smw");
			final List<Future> futureList = repoScanners.stream()
					.map(scanner -> scanner.scanForRepos(repoUrl))
					.collect(Collectors.toList());

			CompositeFuture.all(futureList).setHandler(repoScanResults -> {
				if (repoScanResults.succeeded()) {
					final Set<Repo> blah = repoScanResults.result().list().stream()
							.flatMap(rawStream -> ((Set<Repo>) rawStream).stream())
							.collect(Collectors.toSet());
					System.out.println("future response: " + repoScanResults.result().list());
					response.end(Json.encodePrettily(blah));
				}
			});
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