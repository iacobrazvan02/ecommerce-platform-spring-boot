package com.electrotech.store.service;

import com.electrotech.store.model.Product;
import com.electrotech.store.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getTopProducts() {
        return productRepository.findTop3MostViewed();
    }

    public String getAIPrediction() {
        try {
            ProcessBuilder pb = new ProcessBuilder("python3", "predict_2026.py");
            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            p.waitFor();

            String result = sb.toString().trim();
            return (!result.isEmpty()) ? result : "Nu se poate calcula";
        } catch (Exception e) {
            return "Eroare la pornirea motorului ML: " + e.getMessage();
        }
    }
}