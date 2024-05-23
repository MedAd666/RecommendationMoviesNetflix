package org.example.projetj2eespringnetflix.service;

import org.example.projetj2eespringnetflix.model.User;
import org.example.projetj2eespringnetflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existByPassword(String password) {
        List<User> matchingUsers = userRepository.findAll()
                .stream()
                .filter(user -> user.getPassword().equals(password))
                .toList();
        return !matchingUsers.isEmpty();
    }

    private boolean isValidEmailAddress(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User addUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("L'adresse email est déjà utilisée.");
        }
        if (!isValidEmailAddress(user.getEmail())) {
            throw new IllegalArgumentException("L'adresse email n'est pas valide.");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("L'utilisateur avec l'ID " + user.getId() + " n'existe pas.");
        }
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
