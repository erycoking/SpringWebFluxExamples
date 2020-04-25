package com.erycoking.movieapi;

import java.time.LocalDateTime;

import com.erycoking.movieapi.models.Movie;
import com.erycoking.movieapi.repositories.MovieRepository;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class DemoApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	ApplicationRunner init(ReactiveMongoOperations operations, MovieRepository movieRepository) {
		return args -> {
			movieRepository.deleteAll().subscribe(null, null, () -> {
				Flux<Movie> movies = Flux
						.just(new Movie(null, "Avenger: Infinity Wars", "Action", LocalDateTime.now()),
								new Movie(null, "Gladiator", "Drama/Action", LocalDateTime.now()),
								new Movie(null, "Black Panther", "Action", LocalDateTime.now()))
						.flatMap(movieRepository::save);

				movies.thenMany(movieRepository.findAll()).subscribe(System.out::println);
			});
		};
	}

}
