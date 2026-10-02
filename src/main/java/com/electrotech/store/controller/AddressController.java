package com.electrotech.store.controller;

import com.electrotech.store.dto.AddressRequest;
import com.electrotech.store.model.Address;
import com.electrotech.store.model.Order;
import com.electrotech.store.model.User;
import com.electrotech.store.repository.AddressRepository;
import com.electrotech.store.repository.OrderRepository;
import com.electrotech.store.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AddressController(AddressRepository addressRepository, UserRepository userRepository, OrderRepository orderRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/user/{userId}")
    public List<Address> getUserAddresses(@PathVariable @NonNull Integer userId) {
        return addressRepository.findByUserId(userId);
    }

    @GetMapping("/all")
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody AddressRequest data) {
        try {
            Integer userId = data.getUserId();
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId este obligatoriu"));
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("Utilizator negăsit"));

            Address address = new Address();
            address.setUser(user);
            address.setStreet(data.getStreet() != null ? data.getStreet() : "");
            address.setCity(data.getCity() != null ? data.getCity() : "");
            address.setZipCode(data.getZipCode() != null ? data.getZipCode() : "");
            address.setIsDefault(data.getIsDefault() != null ? data.getIsDefault() : false);

            Address saved = addressRepository.save(address);
            return ResponseEntity.ok(saved);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable @NonNull Integer id) {
        try {
            // Setăm null pe toate comenzile care referențiază această adresă
            List<Order> orders = orderRepository.findByShippingAddressId(id);
            for (Order order : orders) {
                order.setShippingAddress(null);
                orderRepository.save(order);
            }

            addressRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Adresă ștearsă"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Eroare la ștergerea adresei: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable @NonNull Integer id, @RequestBody AddressRequest data) {
        try {
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Adresa nu a fost găsită"));

            if (data.getStreet() != null) address.setStreet(data.getStreet());
            if (data.getCity() != null) address.setCity(data.getCity());
            if (data.getZipCode() != null) address.setZipCode(data.getZipCode());
            if (data.getIsDefault() != null) address.setIsDefault(data.getIsDefault());

            Address saved = addressRepository.save(address);
            return ResponseEntity.ok(saved);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/set-default")
    public ResponseEntity<?> setDefaultAddress(@PathVariable @NonNull Integer id) {
        try {
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Adresa nu a fost găsită"));

            List<Address> allUserAddresses = addressRepository.findByUserId(address.getUser().getId());
            for (Address a : allUserAddresses) {
                a.setIsDefault(false);
                addressRepository.save(a);
            }

            address.setIsDefault(true);
            Address saved = addressRepository.save(address);
            return ResponseEntity.ok(saved);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

