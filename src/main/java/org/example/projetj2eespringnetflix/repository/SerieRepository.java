package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.Serie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SerieRepository extends MongoRepository<Serie,String> {
    List<Movie> findByTitleContainingIgnoreCase(String title);

    @Query("{ 'genres': { $regex: ?0, $options: 'i' } }")
    List<Serie> findByGenresContaining(String genre);

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Serie> findByTitle(String title);


}
