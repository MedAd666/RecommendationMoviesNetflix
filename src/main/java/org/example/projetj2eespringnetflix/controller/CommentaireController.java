package org.example.projetj2eespringnetflix.controller;

import org.example.projetj2eespringnetflix.model.Commentaire;
import org.example.projetj2eespringnetflix.service.CommentaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commentaires")
public class CommentaireController {

    @Autowired
    private final CommentaireService commentaireService;

    public CommentaireController(CommentaireService commentaireService) {
        this.commentaireService = commentaireService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCommentaire(@RequestBody Commentaire commentaire) {
        // Check if the comment already exists
        boolean exists = commentaireService.findCommentaireByUserAndMedia(commentaire.getEmail(), commentaire.getMediaName()).isPresent();
        if (exists) {
            // Return a conflict response if the comment already exists
            return new ResponseEntity<>("You have already submitted a comment for this media.", HttpStatus.CONFLICT);
        }
        // Add the comment if it doesn't exist
        commentaireService.addCommentaire(commentaire);
        return new ResponseEntity<>("Comment submitted successfully", HttpStatus.CREATED);
    }
}
