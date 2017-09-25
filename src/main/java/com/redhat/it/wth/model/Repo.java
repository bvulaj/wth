package com.redhat.it.wth.model;

import java.net.URI;
import java.net.URISyntaxException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Repo {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private @NonNull String name;
	private @NonNull URI location;

	public Repo(final String name, final String location) {
		this.name = name;
		try {
			this.location = new URI(location);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
