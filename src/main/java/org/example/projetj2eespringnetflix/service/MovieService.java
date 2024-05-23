package org.example.projetj2eespringnetflix.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService  implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;


    public void extraireEtEnregistrerFilms() throws Exception {
        long countFilmsExistants = movieRepository.count();
        if (countFilmsExistants < 100) {
            HttpResponse<String> movies_info = Unirest.get("https://imdb-top-100-movies.p.rapidapi.com/")
                    .header("X-RapidAPI-Key", "e9df521eefmsh7d1cbb8e6883af0p139120jsn70f170426af5")
                    .header("X-RapidAPI-Host", "imdb-top-100-movies.p.rapidapi.com")
                    .asString();

            if (movies_info.getStatus() != 200) {
                throw new Exception("Erreur lors de l'appel de l'API : " + movies_info.getStatusText());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Movie> films = objectMapper.readValue(movies_info.getBody(), new TypeReference<List<Movie>>() {});
            movieRepository.saveAll(films);

            for (int i = 1; i < films.size(); i++) {
                HttpResponse<String> movies_info_details = Unirest.get("https://imdb-top-100-movies.p.rapidapi.com/top"+i)
                        .header("X-RapidAPI-Key", "e9df521eefmsh7d1cbb8e6883af0p139120jsn70f170426af5")
                        .header("X-RapidAPI-Host", "imdb-top-100-movies.p.rapidapi.com")
                        .asString();

                if (movies_info_details.getStatus() != 200) {
                    throw new RuntimeException("Erreur de l'appel de l'API : " + movies_info_details.getStatusText() + " the code =  " + movies_info_details.getStatusText() + "  " + movies_info_details.getStatus());
                }

                JSONObject jsonObject = new JSONObject(movies_info_details.getBody());
                String id = jsonObject.getString("id");
                String trailer_embed_link = jsonObject.getString("trailer_embed_link");
                Optional<Movie> optionalMovie = movieRepository.findById(id);
                if (optionalMovie.isPresent()) {
                    Movie movie = optionalMovie.get();
                    movie.setTrailer_embed_link(trailer_embed_link);
                    movieRepository.save(movie);
                } else {
                    System.out.println("Movie not found!");
                }
            }
        } else {
            System.out.println("La collection 'movies' contient déjà 100 films. Aucun film ajouté.");
        }
    }


    public Movie getMovieByName(String name) {
        return movieRepository.findByTitleContainingIgnoreCase(name);
    }



    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenresContaining(genre);
    }


    public List<Movie> getMoviesByGenres(String genres) {
        return movieRepository.findByGenresContaining(genres);
    }

    @Override
    public void run(String... args) throws Exception {
            extraireEtEnregistrerFilms();
    }
}