package org.example.projetj2eespringnetflix.controller;

import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.Serie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.example.projetj2eespringnetflix.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.SequenceInputStream;
import java.util.List;
import java.util.Optional;

@RestController
public class SerieController {

    private final SerieRepository serieRepository;

    @Autowired
    public SerieController(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @GetMapping("/series")
    public List<Serie> getSeries() {
        return serieRepository.findAll();
    }

    @GetMapping("/serie")
    public Optional<Serie> getMovieById(@RequestParam String id) {
        return serieRepository.findById(id);
    }

}
