package com.redhat.it.wth.model;

import java.net.URI;

public class Repo {

	private final String name;
	private final URI location;

	public Repo(final String name, final URI location) {
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public URI getLocation() {
		return location;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Repo repo = (Repo) o;

		if (name != null ? !name.equals(repo.name) : repo.name != null) return false;
		return location != null ? location.equals(repo.location) : repo.location == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (location != null ? location.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Repo{" +
				"name='" + name + '\'' +
				", location=" + location +
				'}';
	}
}
