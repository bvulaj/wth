
package com.redhat.it.wth.scanner.repo;

import com.redhat.it.wth.model.Repo;
import io.vertx.core.Future;

import java.net.URL;
import java.util.Set;

/**
 * Scans the given URL for repos.
 *
 * Should return an empty set if the URL is not of the type expected
 */
public interface RepoScanner {

	Future<Set<Repo>> scanForRepos(URL sourceUrl);

}
