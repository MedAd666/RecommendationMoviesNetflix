package org.example.projetj2eespringnetflix.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media") // Cette annotation peut être omise si la classe ne correspond pas directement à une collection.
public abstract class Media { // Classe abstraite car on ne crée pas directement des objets Media
    protected String id;
    protected Integer rank;
    protected String title;
    protected String description;
    protected String image;
    protected String big_image;
    protected List<String> genre;
    protected String thumbnail;
    protected String rating;
    protected String imdbid;
    protected String imdb_link;
    @Setter
    protected String trailer_embed_link;
    @Field("media_trailer")
    @Setter
    protected String trailer;
    @Setter
    protected List<String> director;
    @Setter
    protected List<String> writers;
}
