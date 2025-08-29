package com.example.product.fasada;

import com.example.product.entity.ProductDTO;
import com.example.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @RequestMapping(path = "/",method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO, HttpServletRequest httpServletRequest){
        return productService.addProduct(productDTO, httpServletRequest);
    }
}
