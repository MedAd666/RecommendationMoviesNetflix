package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.Commentaire;
import org.example.projetj2eespringnetflix.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CommentaireRepository extends MongoRepository<Commentaire,String> {
    @Query("{ 'email': ?0, 'mediaName': ?1 }")
    Optional<Commentaire> findCommentaireByUserAndMedia(String email, String mediaName);
}
