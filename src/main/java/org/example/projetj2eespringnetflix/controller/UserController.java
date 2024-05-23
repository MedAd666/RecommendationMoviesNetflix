package org.example.projetj2eespringnetflix.controller;

import jakarta.servlet.http.HttpSession;
import org.example.projetj2eespringnetflix.model.User;
import org.example.projetj2eespringnetflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/main")
    public RedirectView mainPage(@ModelAttribute User user, HttpSession session) {
        // Vérifiez si l'utilisateur existe par son email et son mot de passe
        if (userService.existsByEmail(user.getEmail()) && userService.existByPassword(user.getPassword())){
            // Stockez l'email de l'utilisateur dans la session
            session.setAttribute("userEmail", user.getEmail());
            // Redirigez vers la page principale sans ajouter l'email en paramètre
            return new RedirectView("/main.html");
        } else {
            // Redirigez vers la page d'index si l'authentification échoue
            return new RedirectView("/index.html");
        }
    }


    @GetMapping("/email")
    public ResponseEntity<String> getUserEmail(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            return ResponseEntity.ok(userEmail);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user email in session");
        }
    }


    @PostMapping("/create")
    public RedirectView createUser(@ModelAttribute User user) {
        RedirectView redirectView = new RedirectView();
        if (userService.existsByEmail(user.getEmail())) {
            redirectView.setUrl("/index2.html");
            return redirectView;
        }
        userService.addUser(user);
        redirectView.setUrl("/main.html");
        return redirectView;
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        user.setId(id); // Avec MongoDB, l'ID est une chaîne de caractères
        User updatedUser = userService.updateUser(user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.listUsers();
        return ResponseEntity.ok(users);
    }

    // ... autres méthodes de gestion des utilisateurs
}