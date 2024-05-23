package org.example.projetj2eespringnetflix.service;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.Rating;
import org.example.projetj2eespringnetflix.model.Serie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.example.projetj2eespringnetflix.repository.RatingRepository;
import org.example.projetj2eespringnetflix.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private SerieRepository seriesRepository;

    public Set<String> getAllGenres() {
        Set<String> genres = new HashSet<>();

        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            if (movie.getGenre() != null) {
                genres.addAll(movie.getGenre());
            }
        }

        List<Serie> seriesList = seriesRepository.findAll();
        for (Serie series : seriesList) {
            if (series.getGenre() != null) {
                genres.addAll(series.getGenre());
            }
        }

        return genres;
    }

    public Map<String, Integer> getRatingsByUser(String email) {
        Map<String, Integer> ratings = new HashMap<>();
        List<Rating> liste = ratingRepository.findByEmail(email);
        for (Rating rating : liste) {
            ratings.put(rating.getMedia(), rating.getRating());
        }
        return ratings;
    }

    public Map<String, Integer> divideRatingsMovies(String email) {
        Map<String, Integer> ratings = getRatingsByUser(email);
        Map<String, Integer> movieRatings = new HashMap<>();

        for (Map.Entry<String, Integer> entry : ratings.entrySet()) {
            String mediaTitle = entry.getKey();
            Integer rating = entry.getValue();

            if (movieRepository.findByTitle(mediaTitle).size() > 0) {
                movieRatings.put(mediaTitle, rating);
            }
        }

        return movieRatings;
    }

    public Map<String, Integer> divideRatingsSeries(String email) {
        Map<String, Integer> ratings = getRatingsByUser(email);
        Map<String, Integer> seriesRatings = new HashMap<>();

        for (Map.Entry<String, Integer> entry : ratings.entrySet()) {
            String mediaTitle = entry.getKey();
            Integer rating = entry.getValue();

            if (seriesRepository.findByTitle(mediaTitle).size() > 0) {
                seriesRatings.put(mediaTitle, rating);
            }
        }

        return seriesRatings;
    }

    public Map<String, Map<String, Integer>> createGenreVectors(String email) {
        Map<String, Integer> movieRatings = divideRatingsMovies(email);
        Set<String> allGenres = getAllGenres();
        Map<String, Map<String, Integer>> genreVectors = new HashMap<>();

        for (Map.Entry<String, Integer> entry : movieRatings.entrySet()) {
            String movieTitle = entry.getKey();
            Integer rating = entry.getValue();
            List<Movie> movies = movieRepository.findByTitle(movieTitle);
            if (movies != null && !movies.isEmpty()) {
                List<String> genres = movies.get(0).getGenre();
                if (genres != null) {
                    Map<String, Integer> genreVector = new HashMap<>();
                    for (String genre : allGenres) {
                        genreVector.put(genre, genres.contains(genre) ? rating : 0);
                    }
                    genreVectors.put(movieTitle, genreVector);
                }
            }
        }

        return genreVectors;
    }

    public double calculateCosineSimilarity(Map<String, Integer> vec1, Map<String, Integer> vec2) {
        RealVector v1 = new ArrayRealVector(vec1.values().stream().mapToDouble(Number::doubleValue).toArray());
        RealVector v2 = new ArrayRealVector(vec2.values().stream().mapToDouble(Number::doubleValue).toArray());
        return v1.cosine(v2);
    }

    public List<String> recommendTop5MoviesBasedOnSpecificMovie(String email, String referenceMovieTitle) {
        
        Map<String, Map<String, Integer>> userGenreVectors = createGenreVectors(email);
        Set<String> allGenres = getAllGenres();

        List<Movie> allMovies = movieRepository.findAll();
        Movie referenceMovie = movieRepository.findByTitle(referenceMovieTitle).get(0);
        List<String> referenceGenres = referenceMovie.getGenre();
        Map<String, Integer> referenceMovieVector = new HashMap<>();
        for (String genre : allGenres) {
            referenceMovieVector.put(genre, referenceGenres.contains(genre) ? 1 : 0);
        }

        Map<String, Double> similarityScores = new HashMap<>();

        for (Movie movie : allMovies) {
            if (movie.getTitle() != null && !movie.getTitle().equals(referenceMovieTitle)) {
                List<String> genres = movie.getGenre();
                if (genres != null) {
                    Map<String, Integer> movieVector = new HashMap<>();
                    for (String genre : allGenres) {
                        movieVector.put(genre, genres.contains(genre) ? 1 : 0);
                    }
                    double similarity = calculateCosineSimilarity(referenceMovieVector, movieVector);
                    similarityScores.put(movie.getTitle(), similarity);
                }
            }
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }



    public List<String> recommendTop5SeriesBasedOnSpecificSeries(String email, String referenceSerieTitle) {

        Map<String, Map<String, Integer>> userGenreVectors = createGenreVectors(email);
        Set<String> allGenres = getAllGenres();

        List<Serie> allSeries = seriesRepository.findAll();
        Serie referenceSerie = seriesRepository.findByTitle(referenceSerieTitle).get(0);

        List<String> referenceGenres = referenceSerie.getGenre();

        Map<String, Integer> referenceSerieVector = new HashMap<>();
        for (String genre : allGenres) {
            referenceSerieVector.put(genre, referenceGenres.contains(genre) ? 1 : 0);
        }

        Map<String, Double> similarityScores = new HashMap<>();

        for (Serie serie : allSeries) {
            if (serie.getTitle() != null && !serie.getTitle().equals(referenceSerieTitle)) {
                List<String> genres = serie.getGenre();
                if (genres != null) {
                    Map<String, Integer> movieVector = new HashMap<>();
                    for (String genre : allGenres) {
                        movieVector.put(genre, genres.contains(genre) ? 1 : 0);
                    }
                    double similarity = calculateCosineSimilarity(referenceSerieVector, movieVector);
                    similarityScores.put(serie.getTitle(), similarity);
                }
            }
        }

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public void run(String... args) throws Exception {
        // Example usage
       // Set<String> genres = getAllGenres();
       // System.out.println("All genres: " + genres);
        // System.out.println(createGenreVectors("adarrabamine@gmail.com"));
        // System.out.println(recommendTop5MoviesBasedOnSpecificMovie("adarrabamine@gmail.com", "The Godfather"));
        // System.out.println(recommendTop5SeriesBasedOnSpecificSeries("adarrabamine@gmail.com", "Breaking Bad"));



    }
}
