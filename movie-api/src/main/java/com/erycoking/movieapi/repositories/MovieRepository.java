package com.erycoking.movieapi.repositories;

import com.erycoking.movieapi.models.Movie;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieRepository extends ReactiveMongoRepository<Movie, String>{

}