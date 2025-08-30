package com.example.product.service;
import com.example.auth.entity.Response;
import com.example.auth.entity.User;
import com.example.auth.entity.UserInfoDTO;
import com.example.auth.repository.UserRepository;
import com.example.auth.services.JwtService;
import com.example.auth.services.UserService;
import com.example.product.entity.*;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductDetailsRepository;
import com.example.product.repository.ProductRepository;
import com.example.warehouse.entity.Location;
import com.example.warehouse.repository.LocationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        System.out.println(productDTO.getRfid());
        product.setRfid(productDTO.getRfid());
        product.setName(productDTO.getName());
        Category category = categoryRepository.findById((long) productDTO.getCategory()).orElse(null);
        if(category!=null){
            product.setCategory(category);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna kategoria"));
        }
        Location location = locationRepository.findLocationById(productDTO.getLocation()).orElse(null);
        if(location!=null){
            product.setLocation(location);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna lokalizacja"));
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

        ProductDetails productDetails = new ProductDetails();
        productDetails.setDescription(productDTO.getDescription());
        productDetails.setWeight(productDTO.getWeight());
        productDetails.setWidth(productDTO.getWidth());
        productDetails.setHeight(productDTO.getHeight());
        productDetails.setCreated_at(new Timestamp(System.currentTimeMillis()));
        productDetails.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        productDetailsRepository.saveAndFlush(productDetails);
        product.setProductDetails(productDetails);
        productRepository.saveAndFlush(product);
        return ResponseEntity.ok(new Response("Pomyślnie dodano produkt"));
    }

    public ResponseEntity<?> editProduct(ProductDTO productDTO, UUID uuid, HttpServletRequest httpServletRequest) {
        Product product = productRepository.findByUuid(uuid).orElse(null);
        if(product==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne UUID produktu"));
        }
        product.setRfid(productDTO.getRfid());
        product.setName(productDTO.getName());
        Category category = categoryRepository.findById((long) productDTO.getCategory()).orElse(null);
        if(category!=null){
            product.setCategory(category);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna kategoria"));
        }
        Location location = locationRepository.findLocationById(productDTO.getLocation()).orElse(null);
        if(location!=null){
            product.setLocation(location);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna lokalizacja"));
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
        product.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        ProductDetails productDetails = productDetailsRepository.findById(product.getProductDetails().getId()).orElse(null);
        if(productDetails==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne szegóły produktu"));
        }
        productDetails.setDescription(productDTO.getDescription());
        productDetails.setWeight(productDTO.getWeight());
        productDetails.setWidth(productDTO.getWidth());
        productDetails.setHeight(productDTO.getHeight());
        productDetailsRepository.saveAndFlush(productDetails);
        product.setProductDetails(productDetails);
        productRepository.saveAndFlush(product);

        return ResponseEntity.ok(new Response("Pomyślnie edytowano produkt"));
    }

    public ResponseEntity<?> deleteProduct(UUID uuid) {
        Product product = productRepository.findByUuid(uuid).orElse(null);
        if(product==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne UUID produktu"));
        }
        productRepository.delete(product);
        return ResponseEntity.ok(new Response("Usunięto produkt"));
    }

    public ResponseEntity<?> showProducts(int page, int size) {
        Pageable pageable1 = PageRequest.of(page,size);
        Page<ProductInfoDTO> productPage = productRepository.findAll(pageable1).map(
                e->new ProductInfoDTO(
                        e.getUuid(),
                        e.getRfid(),
                        e.getName(),
                        e.getCategory(),
                        e.getLocation(),
                        e.getContractor()
                ));
        if(productPage.hasContent()){
            return ResponseEntity.ok().body(productPage);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Nie udało sie zwrócić produktow"));
        }
    }

    public ResponseEntity<?> showProductDetails(UUID uuid) {
        Product product = productRepository.findByUuid(uuid).orElse(null);
        if(product==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne UUID produktu"));
        }
        ProductDetails productDetails = product.getProductDetails();
        return ResponseEntity.ok(new ProductDetailsInfoDTO(
                productDetails.getDescription(),
                productDetails.getWeight(),
                productDetails.getWidth(),
                productDetails.getHeight(),
                productDetails.getCreated_at(),
                productDetails.getUpdated_at()
        ));
    }

    public ResponseEntity<?> getLocationByRFID(String rfid) {
        Product product = productRepository.findByRfid(rfid).orElse(null);
        if(product==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne RFID produktu"));
        }
        Location location = product.getLocation();
        return ResponseEntity.ok(location);
    }

    public ResponseEntity<?> addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        categoryRepository.saveAndFlush(category);
        return ResponseEntity.ok(new Response("Dodano kategorie"));
    }

    public ResponseEntity<?> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }
}
