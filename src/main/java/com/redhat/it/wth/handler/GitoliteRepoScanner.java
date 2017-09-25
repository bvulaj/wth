
package com.redhat.it.wth.handler;

import com.redhat.it.wth.model.Repo;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class GitoliteRepoScanner implements RepoScanner, NeedsVertx<GitoliteRepoScanner> {

	private Vertx vertx;

	@Override
	public Set<Repo> scanForRepos(final URL sourceUrl) {
		// TODO for now we're just assuming gitolite organization URL's.  Need to validate prior to attempted parsing

		WebClient client = WebClient.create(vertx, new WebClientOptions().setSsl(true));
		final Set<Repo> foundRepos = new HashSet<>();
		client.get(443, "gitolite.corp.redhat.com", "/cgit/it-smw")
				.send(result -> {
					if (result.succeeded()) {
						final HttpResponse<Buffer> response = result.result();
						Document responseDoc = Jsoup.parse(response.bodyAsString());
						Elements repoTableDatas = responseDoc.select("td.sublevel-repo > a");
						System.out.println("found elements: " + repoTableDatas.size());
						System.out.println(repoTableDatas);

						foundRepos.addAll(repoTableDatas.stream()
								.map(repoTableData -> new Repo(repoTableData.select("[title]").text(), repoTableData.select("href").text()))
								.collect(Collectors.toSet()));

						System.out.println(foundRepos);

					}
					else {
						System.out.println("FAILURE! " + result.cause().getMessage());
					}
				});

		return foundRepos;
	}

	@Override
	public GitoliteRepoScanner setVertx(final Vertx vertx) {
		this.vertx = vertx;
		return this;
	}
}
