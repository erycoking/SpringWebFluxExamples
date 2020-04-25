package com.erycoking.movieapi.controllers;

import com.erycoking.movieapi.models.Movie;
import com.erycoking.movieapi.repositories.MovieRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("movies")
public class MovieController {

    private MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public Flux<Movie> getMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<Movie>> getSingleMovie(@PathVariable String id) {
        return movieRepository.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Movie> addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @PutMapping(value = "{id}")
    public Mono<ResponseEntity<Movie>> updateMono(@PathVariable String id, @RequestBody Movie entity) {
        return movieRepository.findById(id).flatMap((existingMovie) -> {
            existingMovie.setName(entity.getName());
            existingMovie.setGenre(entity.getGenre());
            existingMovie.setReleaseDate(entity.getReleaseDate());
            return movieRepository.save(existingMovie);
        }).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public Mono<ResponseEntity<Void>> deleteSingleMovie(@PathVariable String id) {
        return movieRepository.findById(id)
                .flatMap((existingMovie) -> movieRepository.delete(existingMovie)
                        .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteMovie() {
        return movieRepository.deleteAll();
    }

}