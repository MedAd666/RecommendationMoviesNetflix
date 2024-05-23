package org.example.projetj2eespringnetflix.controller;

import org.example.projetj2eespringnetflix.model.Rating;
import org.example.projetj2eespringnetflix.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping("/add")
    public ResponseEntity<String> addRating(@RequestBody Rating rating) {
        // Check if the rating already exists
        boolean exists = ratingService.findRatingByUserAndMedia(rating.getEmail_user(), rating.getMedia()).isPresent();
        if (exists) {
            // Return a conflict response if the rating already exists
            return new ResponseEntity<>("You have already submitted a rating for this media.", HttpStatus.CONFLICT);
        }
        // Add the rating if it doesn't exist
        ratingService.addRating(rating);
        return new ResponseEntity<>("Rating submitted successfully", HttpStatus.CREATED);
    }
}
