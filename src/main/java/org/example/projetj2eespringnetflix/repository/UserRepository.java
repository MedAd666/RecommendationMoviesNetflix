package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> { // Utilisation de String pour l'ID avec MongoDB
    boolean existsByEmail(String email);
}
