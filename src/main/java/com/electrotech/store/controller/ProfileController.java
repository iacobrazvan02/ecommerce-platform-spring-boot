package com.electrotech.store.controller;

import com.electrotech.store.model.Address;
import com.electrotech.store.model.User;
import com.electrotech.store.model.UserProfile;
import com.electrotech.store.repository.AddressRepository;
import com.electrotech.store.repository.UserProfileRepository;
import com.electrotech.store.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(UserRepository userRepository,
                             UserProfileRepository userProfileRepository,
                             AddressRepository addressRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable @NonNull Integer userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit"));

            UserProfile profile = user.getProfile();
            List<Address> addresses = addressRepository.findByUserId(userId);

            return ResponseEntity.ok(Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "role", user.getRole() != null ? user.getRole() : "CLIENT",
                    "firstName", profile != null && profile.getFirstName() != null ? profile.getFirstName() : "",
                    "lastName", profile != null && profile.getLastName() != null ? profile.getLastName() : "",
                    "phone", profile != null && profile.getPhone() != null ? profile.getPhone() : "",
                    "addresses", addresses
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@PathVariable @NonNull Integer userId,
                                            @RequestBody Map<String, String> data) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit"));

            if (data.containsKey("email") && data.get("email") != null && !data.get("email").isBlank()) {
                User existingUser = userRepository.findByEmail(data.get("email"));
                if (existingUser != null && !existingUser.getId().equals(userId)) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Email-ul este deja folosit de alt cont"));
                }
                user.setEmail(data.get("email"));
            }

            userRepository.save(user);

            UserProfile profile = user.getProfile();
            if (profile == null) {
                profile = new UserProfile();
                profile.setUser(user);
            }

            if (data.containsKey("firstName")) profile.setFirstName(data.get("firstName"));
            if (data.containsKey("lastName")) profile.setLastName(data.get("lastName"));
            if (data.containsKey("phone")) profile.setPhone(data.get("phone"));

            userProfileRepository.save(profile);

            return ResponseEntity.ok(Map.of(
                    "message", "Profil actualizat cu succes",
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "role", user.getRole() != null ? user.getRole() : "CLIENT",
                    "firstName", profile.getFirstName() != null ? profile.getFirstName() : "",
                    "lastName", profile.getLastName() != null ? profile.getLastName() : "",
                    "phone", profile.getPhone() != null ? profile.getPhone() : ""
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> changePassword(@PathVariable @NonNull Integer userId,
                                             @RequestBody Map<String, String> data) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit"));

            String currentPassword = data.get("currentPassword");
            String newPassword = data.get("newPassword");

            if (currentPassword == null || newPassword == null || newPassword.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Completează toate câmpurile"));
            }

            if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Parola curentă este incorectă"));
            }

            if (newPassword.length() < 4) {
                return ResponseEntity.badRequest().body(Map.of("error", "Parola nouă trebuie să aibă cel puțin 4 caractere"));
            }

            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "Parola a fost schimbată cu succes"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
