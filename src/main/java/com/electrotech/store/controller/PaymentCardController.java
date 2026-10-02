package com.electrotech.store.controller;

import com.electrotech.store.model.PaymentCard;
import com.electrotech.store.model.User;
import com.electrotech.store.repository.PaymentCardRepository;
import com.electrotech.store.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cards")
public class PaymentCardController {

    private final PaymentCardRepository cardRepository;
    private final UserRepository userRepository;

    public PaymentCardController(PaymentCardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    public List<PaymentCard> getUserCards(@PathVariable @NonNull Integer userId) {
        return cardRepository.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody Map<String, Object> data) {
        try {
            Integer userId = (Integer) data.get("userId");
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId este obligatoriu"));
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NoSuchElementException("Utilizator negasit"));

            String cardNumber = data.get("cardNumber") != null ? data.get("cardNumber").toString().replaceAll("\\s", "") : "";
            String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;

            String brand = "CARD";
            if (cardNumber.startsWith("4")) brand = "VISA";
            else if (cardNumber.startsWith("5")) brand = "MASTERCARD";
            else if (cardNumber.startsWith("3")) brand = "AMEX";

            PaymentCard card = new PaymentCard();
            card.setUser(user);
            card.setCardHolder(data.get("cardHolder") != null ? data.get("cardHolder").toString() : "");
            card.setCardNumberLast4(last4);
            card.setCardExpiry(data.get("cardExpiry") != null ? data.get("cardExpiry").toString() : "");
            card.setCardBrand(brand);
            card.setIsDefault(false);

            PaymentCard saved = cardRepository.save(card);
            return ResponseEntity.ok(saved);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCard(@PathVariable @NonNull Integer id, @RequestBody Map<String, Object> data) {
        try {
            PaymentCard card = cardRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Cardul nu a fost gasit"));

            if (data.containsKey("cardHolder")) card.setCardHolder(data.get("cardHolder").toString());
            if (data.containsKey("cardExpiry")) card.setCardExpiry(data.get("cardExpiry").toString());

            if (data.containsKey("cardNumber")) {
                String cardNumber = data.get("cardNumber").toString().replaceAll("\\s", "");
                String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;
                card.setCardNumberLast4(last4);

                String brand = "CARD";
                if (cardNumber.startsWith("4")) brand = "VISA";
                else if (cardNumber.startsWith("5")) brand = "MASTERCARD";
                else if (cardNumber.startsWith("3")) brand = "AMEX";
                card.setCardBrand(brand);
            }

            PaymentCard saved = cardRepository.save(card);
            return ResponseEntity.ok(saved);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable @NonNull Integer id) {
        try {
            cardRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Card sters"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cardul nu a fost gasit"));
        }
    }

    @PutMapping("/{id}/set-default")
    public ResponseEntity<?> setDefaultCard(@PathVariable @NonNull Integer id) {
        try {
            PaymentCard card = cardRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Cardul nu a fost gasit"));

            List<PaymentCard> allCards = cardRepository.findByUserId(card.getUser().getId());
            for (PaymentCard c : allCards) {
                c.setIsDefault(false);
                cardRepository.save(c);
            }

            card.setIsDefault(true);
            PaymentCard saved = cardRepository.save(card);
            return ResponseEntity.ok(saved);
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
