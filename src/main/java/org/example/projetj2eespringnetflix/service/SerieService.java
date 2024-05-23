package org.example.projetj2eespringnetflix.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.Serie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.example.projetj2eespringnetflix.repository.SerieRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.stereotype.Service;
import com.mashape.unirest.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SerieService implements CommandLineRunner {

    @Autowired
    public SerieRepository serieRepository;



    public void extraireEtEnregistrerSeries() throws Exception {
        long countSeriesExistants = serieRepository.count();
        if (countSeriesExistants < 100) {
            HttpResponse<String> serie_info = Unirest.get("https://imdb-top-100-movies.p.rapidapi.com/series/")
                    .header("X-RapidAPI-Key", "4d11af2bd6msh6905e00c041ed72p1a6cf8jsn9ec8a447a6df")
                    .header("X-RapidAPI-Host", "imdb-top-100-movies.p.rapidapi.com")
                    .asString();

            if (serie_info.getStatus() != 200) {
                throw new Exception("Erreur lors de l'appel de l'API : " + serie_info.getStatusText());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Serie> series = objectMapper.readValue(serie_info.getBody(), new TypeReference<List<Serie>>() {});
            serieRepository.saveAll(series);

            for (int i = 1; i < series.size(); i++) {

                HttpResponse<String> series_info_details = Unirest.get("https://imdb-top-100-movies.p.rapidapi.com/series/top"+i)
                        .header("X-RapidAPI-Key", "4d11af2bd6msh6905e00c041ed72p1a6cf8jsn9ec8a447a6df")
                        .header("X-RapidAPI-Host", "imdb-top-100-movies.p.rapidapi.com")
                        .asString();

                if (series_info_details.getStatus() != 200) {
                    throw new RuntimeException("Erreur de l'appel de l'API : " + series_info_details.getStatusText());
                }

                JSONObject jsonObject = new JSONObject(series_info_details.getBody());
                String id = jsonObject.getString("id");
                String trailer_embed_link = jsonObject.getString("trailer_embed_link");
                //JSONArray directorsArray = jsonObject.getJSONArray("director");
                //JSONArray writersArray = jsonObject.getJSONArray("writers");

                Optional<Serie> optionalSerie = serieRepository.findById(id);
                if (optionalSerie.isPresent()) {
                    Serie serie = optionalSerie.get();
                    serie.setTrailer_embed_link(trailer_embed_link);
                  //serie.setDirector(JSONArrayToList(directorsArray));  // Convert JSONArray to List<String>
                  //serie.setWriters(JSONArrayToList(writersArray));  // Convert JSONArray to List<String>
                    serieRepository.save(serie);
                } else {
                    System.out.println("Movie not found!");
                }
            }
        } else {
            System.out.println("La collection 'series' contient déjà 100 films. Aucun film ajouté.");
        }
    }

    // Helper method to convert JSONArray to List<String>
    private List<String> JSONArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }


    @Override
    public void run(String... args) throws Exception {
        extraireEtEnregistrerSeries();
    }
}
