package org.example.projetj2eespringnetflix.service;

import org.example.projetj2eespringnetflix.model.Commentaire;
import org.example.projetj2eespringnetflix.repository.CommentaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentaireService {

    @Autowired
    private CommentaireRepository commentaireRepository;

    public Commentaire addCommentaire(Commentaire commentaire) {
        return commentaireRepository.save(commentaire);
    }

    public Optional<Commentaire> findCommentaireByUserAndMedia(String email_user, String media) {
        return commentaireRepository.findCommentaireByUserAndMedia(email_user, media);
    }

}
