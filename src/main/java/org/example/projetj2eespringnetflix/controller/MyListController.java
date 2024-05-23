package org.example.projetj2eespringnetflix.controller;

import org.example.projetj2eespringnetflix.model.MyList;
import org.example.projetj2eespringnetflix.service.MyListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/mylist")


public class MyListController {

    private final MyListService myListService;

    @Autowired
    public MyListController(MyListService myListService) {
        this.myListService = myListService;
    }

    @PostMapping("/create")
    public ResponseEntity<MyList> createMyList(@RequestBody MyList myListRequest) {
        String userEmail = myListRequest.getUserEmail();

        // Get the new title from the last element of the favorites list
        String newTitle = myListRequest.getFavorites().get(myListRequest.getFavorites().size() - 1);

        Optional<MyList> optionalMyList = myListService.findByUserEmail(userEmail);
        MyList myList;

        System.out.println("Good Creat My List endpoint !!!!");
        if (optionalMyList.isPresent()) {
            myList = optionalMyList.get();
            if (!myList.getFavorites().contains(newTitle)) {
                myList.getFavorites().add(newTitle);
                MyList updatedMyList = myListService.updateMyList(myList.getId(), newTitle);
                return ResponseEntity.ok(updatedMyList);
            } else {
                return ResponseEntity.status(409).body(myList);
            }
        } else {
            // Create a new MyList with the new title
            myList = new MyList();
            myList.setUserEmail(userEmail);
            myList.setFavorites(Collections.singletonList(newTitle));
            MyList createdMyList = myListService.createMyList(myList);
            return ResponseEntity.ok(createdMyList);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> removeFavorite(@RequestParam String userEmail, @RequestParam String title) {
        try {
            System.out.print("ool");
            myListService.removeFavorite(userEmail, title);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("BBBB");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove the title from the list.");
        }
    }

    @GetMapping("/favorites/details")
    public ResponseEntity<List<Map<String, Object>>> getFavoritesDetails(@RequestParam String userEmail) {
        System.out.println("Fetching favorites details for: " + userEmail);
        List<Map<String, Object>> details = myListService.getFavoriteDetails(userEmail);
        if (details.isEmpty()) {
            System.out.println("No details found for user: " + userEmail);
            return ResponseEntity.notFound().build(); // or ResponseEntity.ok(Collections.emptyList());
        }
        System.out.println(details);
        return ResponseEntity.ok(details);
    }







}
