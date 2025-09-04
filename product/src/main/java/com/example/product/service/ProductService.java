package com.example.product.service;
import com.example.auth.entity.Response;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.services.JwtService;
import com.example.contractorservice.entity.Contractor;
import com.example.contractorservice.repository.ContractorRepository;
import com.example.product.entity.*;
import com.example.product.repository.*;
import com.example.warehouse.entity.Hall;
import com.example.warehouse.entity.Shelf;
import com.example.warehouse.entity.Spot;
import com.example.warehouse.repository.HallRepository;
import com.example.warehouse.repository.ShelfRepository;
import com.example.warehouse.repository.SpotRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final SpotRepository spotRepository;
    private final UserRepository userRepository;
    private final ContractorRepository contractorRepository;
    private final JwtService jwtService;
    private final HallRepository hallRepository;
    private final ShelfRepository shelfRepository;
    private final InventoryRepository inventoryRepository;
    private final TransferRepository transferRepository;
    @Transactional
    public ResponseEntity<?> addProduct(ProductCreateDTO productCreateDTO, HttpServletRequest httpServletRequest){
        Product product = new Product();
        System.out.println(productCreateDTO.getRfid());
        product.setRfid(productCreateDTO.getRfid());
        product.setName(productCreateDTO.getName());
        Category category = categoryRepository.findById((long) productCreateDTO.getCategory()).orElse(null);
        if(category!=null){
            product.setCategory(category);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna kategoria"));
        }
        Spot spot = spotRepository.findSpotById(productCreateDTO.getSpot()).orElse(null);
        if(spot!=null && spot.is_free()){
            product.setSpot(spot);
            spotRepository.changeState(false,spot.getId());
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna lokalizacja"));
        }
        Contractor contractor = contractorRepository.findById((long) productCreateDTO.getContractor()).orElse(null);
        if(contractor!=null){
            product.setContractor(contractor);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawny kontrahent"));
        }

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
        productDetails.setDescription(productCreateDTO.getDescription());
        productDetails.setWeight(productCreateDTO.getWeight());
        productDetails.setWidth(productCreateDTO.getWidth());
        productDetails.setHeight(productCreateDTO.getHeight());
        productDetails.setCreated_at(new Timestamp(System.currentTimeMillis()));
        productDetails.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        productDetailsRepository.saveAndFlush(productDetails);
        product.setProductDetails(productDetails);
        productRepository.saveAndFlush(product);
        return ResponseEntity.ok(new Response("Pomyślnie dodano produkt"));
    }
    @Transactional
    public ResponseEntity<?> editProduct(ProductEditDTO productEditDTO, UUID uuid, HttpServletRequest httpServletRequest) {
        Product product = productRepository.findByUuid(uuid).orElse(null);
        if(product==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne UUID produktu"));
        }
        product.setRfid(productEditDTO.getRfid());
        product.setName(productEditDTO.getName());
        Category category = categoryRepository.findById((long) productEditDTO.getCategory()).orElse(null);
        if(category!=null){
            product.setCategory(category);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna kategoria"));
        }
        Contractor contractor = contractorRepository.findById((long) productEditDTO.getContractor()).orElse(null);
        if(contractor!=null){
            product.setContractor(contractor);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawny kontrahent"));
        }

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
        productDetails.setDescription(productEditDTO.getDescription());
        productDetails.setWeight(productEditDTO.getWeight());
        productDetails.setWidth(productEditDTO.getWidth());
        productDetails.setHeight(productEditDTO.getHeight());
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
        productDetailsRepository.delete(product.getProductDetails());
        spotRepository.changeState(true,product.getSpot().getId());
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
                        e.getSpot(),
                        e.getContractor().getAccount_manager().getUsername(),
                        e.getUpdated_at()
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
        Spot spot = product.getSpot();
        return ResponseEntity.ok(spot);
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
    @Transactional
    public ResponseEntity<?> transfer(HttpServletRequest httpServletRequest,TrasnferDTO trasnferDTO) {
        Product product = productRepository.findByRfid(trasnferDTO.getRFID()).orElse(null);
        if(product!=null){
            Spot spot = spotRepository.findSpotById(trasnferDTO.getSpot()).orElse(null);
            if(spot!=null && spot.is_free()){
                User user = userRepository.findUserByUsername(
                        jwtService.getSubject(
                                Arrays.stream(httpServletRequest.getCookies()).filter(
                                    e->e.getName().equals("Authorization")
                                ).toList()
                                        .get(0)
                                        .getValue()
                        )
                ).orElse(null);

                Transfer transfer = new Transfer();
                transfer.setDate(new Timestamp(System.currentTimeMillis()));
                transfer.setProduct(product);
                transfer.setUser(user);
                transfer.setSpot_from(product.getSpot());
                transfer.setSpot_to(spot);
                transferRepository.saveAndFlush(transfer);

                spotRepository.changeState(true,product.getSpot().getId());
                product.setSpot(spot);
                productRepository.saveAndFlush(product);
                spotRepository.changeState(false,spot.getId());

                return ResponseEntity.ok(new Response("Dokonano przesuniecia magazynowego produktu"));
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawna lokalizacja"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne RFID produktu"));
        }
    }
    public ResponseEntity<?> getProductsOnShelves(UUID uuid) {
        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        InventoryData inventoryDataSend = new InventoryData();
        if(hall!=null){
            List<Shelf> shelfs = shelfRepository.findAllByHall(hall);
            if(shelfs.size()>0){
                for(Shelf shelf: shelfs){
                    List<Spot> spots = spotRepository.findAllByShelf(shelf).stream().filter(
                            e->!e.is_free()
                    ).toList();
                    List<ProductInventoryDTO> products = new ArrayList<>();
                    for(Spot spot: spots){
                        Product product = productRepository.findBySpot(spot).orElse(null);
                        if(product!=null){
                            products.add(new ProductInventoryDTO(
                                    product.getUuid(),
                                    product.getRfid(),
                                    product.getName(),
                                    product.getCategory().getName(),
                                    product.getSpot().getId(),
                                    product.getContractor().getName(),
                                    product.getUpdated_at(),
                                    "",
                                    true
                            ));
                        }
                    }
                    inventoryDataSend.getInventory().put(shelf.getName(),products);
                }
                return ResponseEntity.ok(inventoryDataSend);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new org.example.entity.Response("Brak regalow w hali"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new org.example.entity.Response("Brak produktow w hali"));
        }
    }

    public ResponseEntity<?> inventory(HttpServletRequest httpServletRequest,UUID uuid, InventoryData inventoryData) {
        HashMap<String, List<ProductInventoryDTO>> inventory = inventoryData.getInventory();
        Set<String> shelfnames = inventory.keySet();
        Workbook workbook = new XSSFWorkbook();
        String time = new Timestamp(System.currentTimeMillis()).toString().replace(":","");
        Sheet sheet = workbook.createSheet("Inwentaryzacja "+time);
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("UUID");
        header.createCell(1).setCellValue("RFID");
        header.createCell(2).setCellValue("NAME");
        header.createCell(3).setCellValue("CATEGORY");
        header.createCell(4).setCellValue("SPOT");
        header.createCell(5).setCellValue("CONTRACTOR");
        header.createCell(6).setCellValue("UPDATED_AT");
        header.createCell(7).setCellValue("NOTE");
        header.createCell(8).setCellValue("IS_CORRECT");
        int counter=1;
        for(String s: shelfnames){
            List<ProductInventoryDTO> listOfProducts = inventory.get(s);
            for(ProductInventoryDTO p: listOfProducts){
                Row row = sheet.createRow(counter);
                row.createCell(0).setCellValue(p.getUuid().toString());
                row.createCell(1).setCellValue(p.getRFID());
                row.createCell(2).setCellValue(p.getName());
                row.createCell(3).setCellValue(p.getCategory());
                row.createCell(4).setCellValue(p.getSpot());
                row.createCell(5).setCellValue(p.getContractor());
                row.createCell(6).setCellValue(p.getUpdated_at().toString());
                row.createCell(7).setCellValue(p.getNote());
                row.createCell(8).setCellValue(p.getIsCorrect());
                counter++;
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try{
            workbook.write(outputStream);
            workbook.close();
        }
        catch (Exception e){
            System.out.println("TTT");
        }

        Hall hall = hallRepository.findHallByUuid(uuid).orElse(null);
        Inventory inventoryEntity = new Inventory();
        inventoryEntity.setDate(new Timestamp(System.currentTimeMillis()));
        inventoryEntity.setHall(hall);
        String username = jwtService.getSubject(Arrays.stream(httpServletRequest.getCookies()).filter(
                e->e.getName().equals("Authorization")
        ).toList().get(0).getValue());
        User user = userRepository.findUserByUsername(username).orElse(null);
        inventoryEntity.setSupervisor(user);
        inventoryRepository.saveAndFlush(inventoryEntity);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=produkty.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(outputStream.toByteArray());
    }
}
