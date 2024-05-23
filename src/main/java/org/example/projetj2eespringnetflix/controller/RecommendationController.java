package org.example.projetj2eespringnetflix.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.Serie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.example.projetj2eespringnetflix.repository.SerieRepository;
import org.example.projetj2eespringnetflix.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController  {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private MovieRepository movieRepository;


    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("/movies/top5")
    public List<Map<String, String>> recommendTop5Movies(@RequestParam String email, @RequestParam String movieTitle, HttpSession session) {
        List<String> movies = recommendationService.recommendTop5MoviesBasedOnSpecificMovie(email, movieTitle);
        List<Map<String, String>> movieDetails = new ArrayList<>();

        for (String movie : movies) {
            List<Movie> t = movieRepository.findByTitle(movie);
            if (!t.isEmpty()) {
                Movie m = t.get(0); // Assuming the first result is the desired one
                Map<String, String> movieDetail = new HashMap<>();
                movieDetail.put("titre", m.getTitle());
                movieDetail.put("big_image", m.getBig_image());
                movieDetails.add(movieDetail);
            }
        }
        // Store movieDetails in session
        session.setAttribute("movieDetails", movieDetails);

        return movieDetails;
    }


    @GetMapping("/series/top5")
    public List<Map<String, String>> recommendTop5Series(@RequestParam String email, @RequestParam String serieTitle, HttpSession session) {
        List<String> series = recommendationService.recommendTop5SeriesBasedOnSpecificSeries(email, serieTitle);
        List<Map<String, String>> serieDetails = new ArrayList<>();

        for (String serie : series) {
            List<Serie> t = serieRepository.findByTitle(serie);
            if (!t.isEmpty()) {
                Serie s = t.get(0); // Assuming the first result is the desired one
                Map<String, String> serieDetail = new HashMap<>();
                serieDetail.put("titre", s.getTitle());
                serieDetail.put("big_image", s.getBig_image());
                serieDetails.add(serieDetail);
            }
        }
        session.setAttribute("serieDetails", serieDetails);
        return serieDetails;
    }

    @GetMapping("/movies/session")
    public List<Map<String, String>> getMovieDetailsFromSession(HttpSession session) {
        return (List<Map<String, String>>) session.getAttribute("movieDetails");
    }

    @GetMapping("/series/session")
    public List<Map<String, String>> getSerieDetailsFromSession(HttpSession session) {
        return (List<Map<String, String>>) session.getAttribute("serieDetails");
    }



}
