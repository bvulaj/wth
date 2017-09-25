package com.redhat.it.wth.handler;

import com.redhat.it.wth.model.Repo;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class StaticValueRepoScanner implements RepoScanner {

	private final Set<Repo> staticRepolist;

	public StaticValueRepoScanner(final Set<Repo> staticRepolist) {
		this.staticRepolist = staticRepolist;
	}

	@Override
	public Set<Repo> scanForRepos(final URL sourceUrl) {
		return new HashSet<>(staticRepolist);
	}
}
