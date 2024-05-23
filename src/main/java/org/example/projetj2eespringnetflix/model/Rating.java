package org.example.projetj2eespringnetflix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ratings")
public class Rating {
    @Id
    private String id;
    private String media;
    private String email_user;
    private int rating;
}
