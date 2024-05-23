package org.example.projetj2eespringnetflix.model;

import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "series")
public class Serie extends Media { // Hérite de Media
    private String year;
    @Field("serie_trailer")
    @Setter
    private String trailer;
}
