package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.RecommendationSystem;
import org.example.projetj2eespringnetflix.model.Serie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RecommendationRepository extends MongoRepository<RecommendationSystem,String> {
    @Query("{ 'userEmail': { $regex: ?0, $options: 'i' } }")
   List<Movie> findByUserEmail(String email);
}
