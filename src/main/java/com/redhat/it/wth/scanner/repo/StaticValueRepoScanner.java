package com.redhat.it.wth.scanner.repo;

import com.redhat.it.wth.model.Repo;
import io.vertx.core.Future;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class StaticValueRepoScanner implements RepoScanner {

	private Set<Repo> staticRepolist;

	public StaticValueRepoScanner() {
		try {
			staticRepolist = Collections.singleton(new Repo("darcy", new URI("ssh://gitolite.corp.redhat.com/it-smw/darcy.git")));
		} catch (URISyntaxException e) {
			// TODO throw something
			staticRepolist = Collections.emptySet();
		}
	}

	public StaticValueRepoScanner(final Set<Repo> staticRepolist) {
		this.staticRepolist = staticRepolist;
	}

	@Override
	public Future<Set<Repo>> scanForRepos(final URL sourceUrl) {
		final Future scanFuture = Future.future();
		scanFuture.complete(staticRepolist);
		return scanFuture;
	}
}
