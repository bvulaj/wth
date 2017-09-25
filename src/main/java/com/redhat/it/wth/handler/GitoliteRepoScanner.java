
package com.redhat.it.wth.handler;

import com.redhat.it.wth.model.Repo;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

@Component
public class GitoliteRepoScanner implements RepoScanner, NeedsVertx<GitoliteRepoScanner> {

	private Vertx vertx;

	@Override
	public Set<Repo> scanForRepos(final URL sourceUrl) {
		// TODO for now we're just assuming gitolite organization URL's.  Need to validate prior to attempted parsing

		WebClient client = WebClient.create(vertx, new WebClientOptions().setSsl(true));
		client.get(443, "gitolite.corp.redhat.com", "/cgit/it-smw")
				.send(result -> {
					if (result.succeeded()) {
						System.out.println("we have had great success!");
						final HttpResponse<Buffer> response = result.result();
						System.out.println(response.bodyAsString());
					}
					else {
						System.out.println("FAILURE! " + result.cause().getMessage());
					}
				});

		return Collections.emptySet();
	}

	@Override
	public GitoliteRepoScanner setVertx(final Vertx vertx) {
		this.vertx = vertx;
		return this;
	}
}
