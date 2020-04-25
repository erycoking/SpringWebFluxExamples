package com.howtodoinjava.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.howtodoinjava.demo.dao.EmployeeRepository;
import com.howtodoinjava.demo.model.Employee;
import com.howtodoinjava.demo.service.SequentialGeneratorService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class WebfluxFunctionalApp implements ApplicationRunner {

	@Autowired
	EmployeeRepository repository;

	@Autowired
	SequentialGeneratorService generatorService;

	public static void main(String[] args) {
		SpringApplication.run(WebfluxFunctionalApp.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		repository.deleteAll().subscribe(null, null, () -> {
			Flux<Employee> empFlux = Flux
					.just(new Employee(generatorService.generateSequence(Employee.SEQUENCE_NAME), "erick", 3000),
							new Employee(generatorService.generateSequence(Employee.SEQUENCE_NAME), "nesh", 3000),
							new Employee(generatorService.generateSequence(Employee.SEQUENCE_NAME), "tobi", 3000))
					.flatMap(repository::save);
			empFlux.thenMany(repository.findAll()).subscribe(System.out::println);
		});
	}
}