package com.example.product.service;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.services.JwtService;
import com.example.auth.services.UserService;
import com.example.product.entity.Product;
import com.example.product.entity.ProductDTO;
import com.example.product.entity.ProductDetails;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductDetailsRepository;
import com.example.product.repository.ProductRepository;
import com.example.warehouse.entity.Location;
import com.example.warehouse.repository.LocationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public ResponseEntity<?> addProduct(ProductDTO productDTO, HttpServletRequest httpServletRequest){
        Product product = new Product();
        product.setRfid(productDTO.getRFID());
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());

        ProductDetails productDetails = new ProductDetails();
        productDetails.setDescription(productDTO.getDescription());
        productDetails.setWeight(productDTO.getWeight());
        productDetails.setWidth(productDTO.getWidth());
        productDetails.setHeight(productDTO.getHeight());
        productDetails.setCreated_at(new Timestamp(System.currentTimeMillis()));
        productDetails.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        productDetailsRepository.saveAndFlush(productDetails);

        product.setProductDetails(productDetails);
        Location location = locationRepository.findLocationById(productDTO.getLocation()).orElse(null);
        if(location!=null){
            product.setLocation(location);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zla lokalizacja");
        }
        product.setContractor(1);

        List<Cookie> cookies = Arrays.stream(httpServletRequest.getCookies()).toList();
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("Authorization")){
                String username = jwtService.getSubject(cookie.getValue());
                User user = userRepository.findUserByUsername(username).orElse(null);
                product.setUser(user);
            }
        }
        product.setCreated_at(new Timestamp(System.currentTimeMillis()));
        product.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        productRepository.saveAndFlush(product);
        return ResponseEntity.ok("Udalo sie dodac produkt jakimś cudem");
    }
}
