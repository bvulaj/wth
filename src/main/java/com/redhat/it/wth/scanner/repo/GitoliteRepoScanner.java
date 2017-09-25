
package com.redhat.it.wth.scanner.repo;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertx.core.Future;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.redhat.it.wth.handler.NeedsVertx;
import com.redhat.it.wth.model.Repo;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

@Component
public class GitoliteRepoScanner implements RepoScanner, NeedsVertx<GitoliteRepoScanner> {

	private Vertx vertx;

	@Override
	public Future<Set<Repo>> scanForRepos(final URL sourceUrl) {
		// TODO for now we're just assuming gitolite organization URL's.  Need to validate prior to attempted parsing
		final Future<Set<Repo>> repoScanFuture = Future.future();

		WebClient client = WebClient.create(vertx, new WebClientOptions().setSsl(true));
		final Set<Repo> foundRepos = new HashSet<>();
		client.get(443, "gitolite.corp.redhat.com", "/cgit/it-smw") // TODO: There is a `getAbs` as well which I think we'll probably end up using
				.send(result -> {
					if (result.succeeded()) {
						final HttpResponse<Buffer> response = result.result();
						Document responseDoc = Jsoup.parse(response.bodyAsString());
						Elements repoTableDatas = responseDoc.select("td.sublevel-repo > a");

						// TODO convert these relative paths into full paths
						foundRepos.addAll(repoTableDatas.stream()
								.map(repoTableData -> new Repo(repoTableData.select("[title]").text(), repoTableData.select("[href]").text()))
								.collect(Collectors.toSet()));
						repoScanFuture.complete(foundRepos);

					}
					else {
						repoScanFuture.fail(result.cause().getMessage());
					}
				});

		return repoScanFuture;
	}

	@Override
	public GitoliteRepoScanner setVertx(final Vertx vertx) {
		this.vertx = vertx;
		return this;
	}
}
