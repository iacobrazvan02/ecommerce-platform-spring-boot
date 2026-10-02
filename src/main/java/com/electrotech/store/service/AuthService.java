package com.electrotech.store.service;

import com.electrotech.store.model.User;
import com.electrotech.store.model.UserProfile;
import com.electrotech.store.repository.UserRepository;
import com.electrotech.store.repository.UserProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(String email, String password, String firstName, String lastName) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password)); // BCrypt hashing
        user.setRole("CLIENT");

        User savedUser = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setUser(savedUser);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);

        userProfileRepository.save(profile);

        return savedUser;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null; // Login eșuat
    }
}