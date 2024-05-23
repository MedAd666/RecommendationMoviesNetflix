package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating, String> {

    @Query("{ 'email_user': ?0, 'media': ?1 }")
    Optional<Rating> findByEmailUserAndMedia(String emailUser, String media);

    @Query("{ 'email_user' : ?0 }")
    List<Rating> findByEmail(String email);

}
