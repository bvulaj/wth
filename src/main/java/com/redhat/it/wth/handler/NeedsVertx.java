
package com.redhat.it.wth.handler;

import io.vertx.core.Vertx;

/**
 * Scans the given URL for repos.
 *
 * Should return an empty set if the URL is not of the type expected
 */
public interface NeedsVertx<T extends NeedsVertx<?>> {

	T setVertx(Vertx vertx);

}
