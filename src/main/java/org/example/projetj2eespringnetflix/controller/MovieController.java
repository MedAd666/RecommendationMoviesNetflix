package org.example.projetj2eespringnetflix.controller;

import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class MovieController {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }


    @GetMapping("/movie")
    public Optional<Movie> getMovieById(@RequestParam String id) {
        return movieRepository.findById(id);
    }



}
