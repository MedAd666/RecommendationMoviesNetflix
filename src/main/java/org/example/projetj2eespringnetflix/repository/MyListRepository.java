package org.example.projetj2eespringnetflix.repository;

import org.example.projetj2eespringnetflix.model.MyList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MyListRepository extends MongoRepository<MyList, String> {
    @Query("{ 'userEmail': ?0 }")
    Optional<MyList> findByUserEmail(String userEmail);
}
