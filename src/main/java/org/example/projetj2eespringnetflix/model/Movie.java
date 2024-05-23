
package org.example.projetj2eespringnetflix.model;

import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Document(collection = "movies")
public class Movie extends Media { // Hérite de Media
    private Integer year; // Attributs spécifiques à Movie
    @Setter
    private String overview; // Attributs spécifiques à Movie
}