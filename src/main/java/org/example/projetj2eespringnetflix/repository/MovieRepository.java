package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Movie findByTitleContainingIgnoreCase(String title);
    @Query("{ 'genres': { $regex: ?0, $options: 'i' } }")
    List<Movie> findByGenresContaining(String genre);
    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Movie> findByTitle(String title);

}
