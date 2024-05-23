package org.example.projetj2eespringnetflix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "commentary")
public class Commentaire {
    @Id
    private String id;
    private String email;
    private String texte;
    private String mediaName;

}
