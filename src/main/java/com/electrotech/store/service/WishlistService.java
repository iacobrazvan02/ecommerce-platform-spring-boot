package com.electrotech.store.service;

import com.electrotech.store.model.Product;
import com.electrotech.store.model.User;
import com.electrotech.store.model.Wishlist;
import com.electrotech.store.repository.ProductRepository;
import com.electrotech.store.repository.UserRepository;
import com.electrotech.store.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Wishlist> getWishlist(Integer userId) {
        return wishlistRepository.findByUserId(userId);
    }

    @Transactional
    public Wishlist addToWishlist(Integer userId, Long productId) {
        if (wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new RuntimeException("Produsul este deja în lista de dorințe!");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Userul nu a fost găsit"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produsul nu a fost găsit"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        return wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeFromWishlist(Integer wishlistId, Integer userId) {
        wishlistRepository.deleteByIdAndUserId(wishlistId, userId);
    }

    @Transactional
    public void clearWishlist(Integer userId) {
        wishlistRepository.deleteByUserId(userId);
    }
}
