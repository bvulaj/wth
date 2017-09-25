
package com.redhat.it.wth.handler;

import com.redhat.it.wth.model.Repo;

import java.net.URL;
import java.util.Set;

public interface RepoScanner {

	public Set<Repo> scanForRepos(URL sourceUrl);

}
