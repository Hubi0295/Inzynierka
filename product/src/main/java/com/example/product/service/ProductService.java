package com.example.product.service;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductDetailsRepository;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductDetailsRepository productDetailsRepository;
}
