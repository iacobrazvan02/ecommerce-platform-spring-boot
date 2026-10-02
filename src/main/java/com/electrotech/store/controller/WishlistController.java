package com.electrotech.store.controller;

import com.electrotech.store.model.Wishlist;
import com.electrotech.store.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{userId}")
    public List<Wishlist> getWishlist(@PathVariable @NonNull Integer userId) {
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody Map<String, Object> data) {
        try {
            Integer userId = (Integer) data.get("userId");
            Long productId = Long.valueOf(data.get("productId").toString());
            Wishlist saved = wishlistService.addToWishlist(userId, productId);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable @NonNull Integer id,
                                                 @RequestParam @NonNull Integer userId) {
        wishlistService.removeFromWishlist(id, userId);
        return ResponseEntity.ok(Map.of("message", "Produs eliminat din lista de dorințe"));
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearWishlist(@PathVariable @NonNull Integer userId) {
        wishlistService.clearWishlist(userId);
        return ResponseEntity.ok(Map.of("message", "Lista de dorințe a fost golită"));
    }
}
