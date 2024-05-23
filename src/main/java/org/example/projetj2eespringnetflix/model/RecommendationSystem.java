package org.example.projetj2eespringnetflix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recommendations")
public class RecommendationSystem {
    @Id
    private String id;

    private String userEmail;

    private List<Map<String, String>> recommendedMovies;

    private List<Map<String, String>> recommendedSeries;
}
