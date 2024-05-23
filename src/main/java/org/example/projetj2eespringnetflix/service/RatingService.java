package org.example.projetj2eespringnetflix.service;

import org.example.projetj2eespringnetflix.model.Rating;
import org.example.projetj2eespringnetflix.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Optional<Rating> findRatingByUserAndMedia(String email_user, String media) {
        return ratingRepository.findByEmailUserAndMedia(email_user, media);
    }

}
