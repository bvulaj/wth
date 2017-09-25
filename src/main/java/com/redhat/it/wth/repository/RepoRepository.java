package com.redhat.it.wth.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.redhat.it.wth.model.Repo;

public interface RepoRepository extends PagingAndSortingRepository<Repo, Long> {

}
