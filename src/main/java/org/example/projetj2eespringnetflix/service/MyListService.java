package org.example.projetj2eespringnetflix.service;

import org.example.projetj2eespringnetflix.model.Movie;
import org.example.projetj2eespringnetflix.model.MyList;
import org.example.projetj2eespringnetflix.model.Serie;
import org.example.projetj2eespringnetflix.repository.MovieRepository;
import org.example.projetj2eespringnetflix.repository.MyListRepository;
import org.example.projetj2eespringnetflix.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MyListService {

    private final MyListRepository myListRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SerieRepository seriesRepository;



    @Autowired
    public MyListService(MyListRepository myListRepository) {
        this.myListRepository = myListRepository;
    }

    public MyList createMyList(MyList myList) {
        return myListRepository.save(myList);
    }
    
    public Optional<MyList> findByUserEmail(String userEmail){
        return myListRepository.findByUserEmail(userEmail);
    }


    public MyList updateMyList(String id, String titre) {
        MyList myList = getMyListById(id);
        if (!myList.getFavorites().contains(titre)) {
            myList.getFavorites().add(titre);
        }
        return myListRepository.save(myList);
    }

    public MyList getMyListById(String id) {
        Optional<MyList> myList = myListRepository.findById(id);
        return myList.orElseThrow(() -> new RuntimeException("MyList not found with id " + id));
    }

    public void removeFavorite(String userEmail, String title) {
            Optional<MyList> myList = myListRepository.findByUserEmail(userEmail);
            if (myList.isPresent()) {
                MyList myList1 = myList.get();
                List<String> favorites = myList1.getFavorites();
                favorites.remove(title);
                myList1.setFavorites(favorites);
                System.out.println(myList1.getFavorites());
                myListRepository.save(myList1);
            }else{
                throw new RuntimeException("MyList not found with id " + userEmail);
            }
    }

    public List<String> getFavoritesByUserEmail(String userEmail) {
        Optional<MyList> optionalMyList = myListRepository.findByUserEmail(userEmail);
        return optionalMyList.map(MyList::getFavorites).orElse(Collections.emptyList());
    }

    public List<Map<String, Object>> getFavoriteDetails(String userEmail) {
        List<String> favorites = getFavoritesByUserEmail(userEmail);
        List<Map<String, Object>> favoriteDetails = new ArrayList<>();

        for (String title : favorites) {
            // Recherche dans les films
            List<Movie> movies = movieRepository.findByTitle(title);
            if (!movies.isEmpty()) {
                Movie movie = movies.get(0);  // Just take the first result if multiple
                Map<String, Object> detail = new HashMap<>();
                detail.put("titre", movie.getTitle());
                detail.put("big_image", movie.getBig_image());
                favoriteDetails.add(detail);
            }

            // Recherche dans les séries
            List<Serie> seriesList = seriesRepository.findByTitle(title);
            if (!seriesList.isEmpty()) {
                Serie series = seriesList.get(0);  // Just take the first result if multiple
                Map<String, Object> detail = new HashMap<>();
                detail.put("titre", series.getTitle());
                detail.put("big_image", series.getBig_image());
                favoriteDetails.add(detail);
            }
        }

        return favoriteDetails;
    }


}
