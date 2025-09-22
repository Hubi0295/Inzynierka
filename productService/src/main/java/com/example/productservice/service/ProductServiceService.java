package com.example.productservice.service;

import com.example.auth.entity.Response;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.services.JwtService;
import com.example.contractorservice.entity.Contractor;
import com.example.contractorservice.repository.ContractorRepository;
import com.example.product.entity.ActionType;
import com.example.product.entity.Product;
import com.example.product.entity.ProductHistory;
import com.example.product.entity.ProductInfoDTO;
import com.example.product.repository.ProductHistoryRespository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import com.example.productservice.entity.*;
import com.example.productservice.repository.ProductIssueRepository;
import com.example.productservice.repository.ProductReceiptRepository;
import com.example.warehouse.repository.SpotRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceService {
    private final ProductReceiptRepository productReceiptRepository;
    private final ProductIssueRepository productIssueRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ContractorRepository contractorRepository;
    private final ProductHistoryRespository productHistoryRespository;
    private final SpotRepository spotRepository;
    @Transactional
    public ResponseEntity<?> createProductReceipt(ProductReceiptDTO productReceiptDTO, HttpServletRequest httpServletRequest) {
        String username = jwtService.getSubject(Arrays.stream(httpServletRequest.getCookies()).filter(e->e.getName().equals("Authorization")).findFirst().get().getValue());
        User user = userRepository.findUserByUsername(username).orElse(null);
        if(user!=null){
            ProductReceipt productReceipt = new ProductReceipt();
            productReceipt.setUser(user);
            Contractor contractor = contractorRepository.findById(productReceiptDTO.getContractor()).orElse(null);
            if(contractor!=null){
                productReceipt.setContractor(contractor);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                productReceipt.setCreated_at(timestamp);
                productReceipt.setUpdated_at(timestamp);
                productReceipt.setDocument_number("DokumentPrzyjecia "+timestamp);
                productReceiptRepository.saveAndFlush(productReceipt);
                List<ProductInfoDTO> products = new ArrayList<>();
                for(UUID productId: productReceiptDTO.getProducts()){
                    Product product = productRepository.findByUuid(productId).orElse(null);
                    if(product!=null){
                        if(product.getProduct_receipt()!=0 || product.is_active()){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany produkt"));
                        }
                        product.setProduct_receipt(productReceipt.getId());
                        product.set_active(true);
                        saveHistory(product,ActionType.RECEIPT,product.getUser());
                        products.add(new ProductInfoDTO(product.getUuid(),product.getRfid(),product.getName(),product.getCategory(),product.getSpot(),product.getContractor().getName(),product.getUpdated_at()));
                    }
                    else{
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany produkt"));
                    }
                }
                XWPFDocument document = getDocumentReceipt(productReceipt, products,user, ActionType.RECEIPT);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if(document!=null){
                    try{
                        document.write(outputStream);
                        document.close();
                    }
                    catch (Exception e){
                        return ResponseEntity.ok(new Response("Udalo się utworzyć przyjęcie, ale wystąpił błąd przy generowaniu dokumentu"));
                    }
                }
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=przyjecie.docx")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(outputStream.toByteArray());
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany kontrahent"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawny uzytkownik"));
        }
    }



    @Transactional
    public ResponseEntity<?> editReceipt(ProductReceiptDTO productReceiptDTO, HttpServletRequest httpServletRequest, UUID uuid) {
        ProductReceipt productReceipt = productReceiptRepository.findByUuid(uuid).orElse(null);
        if(productReceipt!=null){
            String username = jwtService.getSubject(Arrays.stream(httpServletRequest.getCookies()).filter(e->e.getName().equals("Authorization")).findFirst().get().getValue());
            User user = userRepository.findUserByUsername(username).orElse(null);
            if(user!=null) {
                productReceipt.setUser(user);
                Contractor contractor = contractorRepository.findById(productReceiptDTO.getContractor()).orElse(null);
                if (contractor != null) {
                    productReceipt.setContractor(contractor);
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    productReceipt.setDocument_number("EdytowanePrzyjecie " + time);
                    productReceipt.setUpdated_at(time);
                    productReceiptRepository.saveAndFlush(productReceipt);
                    productRepository.findByProduct_receipt_id(productReceipt.getId()).forEach(e->{
                        e.setProduct_receipt(0);
                        e.set_active(false);
                    });
                    List<ProductInfoDTO> products = new ArrayList<>();
                    for(UUID id: productReceiptDTO.getProducts()){
                        Product product = productRepository.findByUuid(id).orElse(null);
                        if(product!=null && product.getProduct_receipt()==0 && !product.is_active()){
                            product.setProduct_receipt(productReceipt.getId());
                            product.set_active(true);
                            saveHistory(product,ActionType.RECEIPT,product.getUser());
                            products.add(new ProductInfoDTO(product.getUuid(),product.getRfid(),product.getName(),product.getCategory(),product.getSpot(),product.getContractor().getName(),product.getUpdated_at()));
                        }
                        else{
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany produkt"));
                        }
                    }
                    XWPFDocument document = getDocumentReceipt(productReceipt, products,user, ActionType.EDITRECEIPT);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    if(document!=null){
                        try{
                            document.write(outputStream);
                            document.close();
                        }
                        catch (Exception e){
                            return ResponseEntity.ok(new Response("Udalo się utworzyć edycje przyjęcia, ale wystąpił błąd przy generowaniu dokumentu"));
                        }
                    }
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=przyjecieEdit.docx")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(outputStream.toByteArray());
                }
                else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany kontrahent"));
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany uzytkownik"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zle podany uuid Przyjecia"));
        }
    }

    public ResponseEntity<?> deleteReceipt(UUID uuid) {
        ProductReceipt productReceipt = productReceiptRepository.findByUuid(uuid).orElse(null);
        if(productReceipt!=null){
            productRepository.findByProduct_receipt_id(productReceipt.getId()).forEach(e->{
                e.setProduct_receipt(0);
                e.set_active(false);
            });
            productReceiptRepository.delete(productReceipt);
            return ResponseEntity.ok(new Response("Usunięto przyjęcie magazynowe"));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany numer przyjecia"));
        }
    }
    public ResponseEntity<?> showAllReceipts() {
        List<ProductReceiptInfo>  productReceiptInfos = new ArrayList<>();
        productReceiptRepository.findAll().forEach(
                e->{
                    productReceiptInfos.add(
                            new ProductReceiptInfo(
                                    e.getUuid(),
                                    e.getContractor().getName(),
                                    e.getCreated_at(),
                                    e.getUpdated_at(),
                                    e.getDocument_number()
                            )
                    );
                }
        );
        if(!productReceiptInfos.isEmpty()){
            return ResponseEntity.ok(productReceiptInfos);
        }
        else{
            return ResponseEntity.ok(new Response("Brak przyjec"));
        }
    }

    public ResponseEntity<?> showReceiptDetails(UUID uuid) {
        ProductReceiptDetails productReceiptDetails = new ProductReceiptDetails();
        ProductReceipt productReceipt = productReceiptRepository.findByUuid(uuid).orElse(null);
        if(productReceipt!=null){
            productReceiptDetails.setUuid(productReceipt.getUuid());
            productReceiptDetails.setContractor(productReceipt.getContractor().getName());
            productReceiptDetails.setUpdated_at(productReceipt.getUpdated_at());
            productReceiptDetails.setCreated_at(productReceipt.getCreated_at());
            productReceiptDetails.setDocument_number(productReceipt.getDocument_number());
            List<ProductInfoDTO> productsInfo = new ArrayList<>();
            productRepository.findByProduct_receipt_id(productReceipt.getId()).forEach(
                    e->productsInfo.add(new ProductInfoDTO(
                            e.getUuid(),
                            e.getRfid(),
                            e.getName(),
                            e.getCategory(),
                            e.getSpot(),
                            e.getContractor().getName(),
                            e.getUpdated_at()
                    ))
            );
            productReceiptDetails.setProducts(productsInfo);
            return ResponseEntity.ok(productReceiptDetails);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne uuid"));
        }
    }




    @Transactional
    public ResponseEntity<?> createProductIssue(ProductIssueDTO productIssueDTO, HttpServletRequest httpServletRequest){
        String username = jwtService.getSubject(Arrays.stream(httpServletRequest.getCookies()).filter(e->e.getName().equals("Authorization")).findFirst().get().getValue());
        User user = userRepository.findUserByUsername(username).orElse(null);
        if(user!=null){
            ProductIssue productIssue = new ProductIssue();
            productIssue.setUser(user);
            Contractor contractor = contractorRepository.findById(productIssueDTO.getContractor()).orElse(null);
            if(contractor!=null){
                productIssue.setContractor(contractor);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                productIssue.setCreated_at(timestamp);
                productIssue.setUpdated_at(timestamp);
                productIssue.setDocument_number("DokumentWydania "+timestamp);
                productIssueRepository.saveAndFlush(productIssue);
                List<ProductInfoDTO> products = new ArrayList<>();
                for(UUID productId: productIssueDTO.getProducts()){
                    Product product = productRepository.findByUuid(productId).orElse(null);
                    if(product!=null){
                        if(product.getProduct_issue()!=0 || !product.is_active()){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany produkt"));
                        }
                        product.setProduct_issue(productIssue.getId());
                        product.set_active(false);
                        saveHistory(product,ActionType.ISSUE,product.getUser());
                        spotRepository.changeState(true,product.getSpot().getId());
                        spotRepository.flush();
                        products.add(new ProductInfoDTO(product.getUuid(),product.getRfid(),product.getName(),product.getCategory(),product.getSpot(),product.getContractor().getName(),product.getUpdated_at()));
                    }
                    else{
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany produkt"));
                    }
                }
                XWPFDocument document = getDocumentIssue(productIssue, products,user, ActionType.ISSUE);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                if(document!=null){
                    try{
                        document.write(outputStream);
                        document.close();
                    }
                    catch (Exception e){
                        return ResponseEntity.ok(new Response("Udalo się utworzyć wydanie, ale wystąpił błąd przy generowaniu dokumentu"));
                    }
                }
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Wydanie.docx")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(outputStream.toByteArray());
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany kontrahent"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawny uzytkownik"));
        }
    }
    @Transactional
    public ResponseEntity<?> editIssue(ProductIssueDTO productIssueDTO, HttpServletRequest httpServletRequest, UUID uuid) {
        ProductIssue productIssue = productIssueRepository.findByUuid(uuid).orElse(null);
        if(productIssue!=null){
            String username = jwtService.getSubject(Arrays.stream(httpServletRequest.getCookies()).filter(e->e.getName().equals("Authorization")).findFirst().get().getValue());
            User user = userRepository.findUserByUsername(username).orElse(null);
            if(user!=null) {
                productIssue.setUser(user);
                Contractor contractor = contractorRepository.findById(productIssueDTO.getContractor()).orElse(null);
                if (contractor != null) {
                    productIssue.setContractor(contractor);
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    productIssue.setDocument_number("EdytowanePrzyjecie " + time);
                    productIssue.setUpdated_at(time);
                    productIssueRepository.saveAndFlush(productIssue);
                    productRepository.findByProduct_issue_id(productIssue.getId()).forEach(e->{
                        e.setProduct_issue(0);
                        e.set_active(true);
                        spotRepository.changeState(false,e.getSpot().getId());
                    });
                    List<ProductInfoDTO> products = new ArrayList<>();
                    for(UUID id: productIssueDTO.getProducts()){
                        Product product = productRepository.findByUuid(id).orElse(null);
                        if(product!=null && product.getProduct_issue()==0 && product.is_active()){
                            product.setProduct_issue(productIssue.getId());
                            product.set_active(false);
                            saveHistory(product,ActionType.ISSUE,product.getUser());
                            spotRepository.changeState(true,product.getSpot().getId());
                            products.add(new ProductInfoDTO(product.getUuid(),product.getRfid(),product.getName(),product.getCategory(),product.getSpot(),product.getContractor().getName(),product.getUpdated_at()));
                        }
                        else{
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany produkt"));
                        }
                    }
                    XWPFDocument document = getDocumentIssue(productIssue, products,user, ActionType.EDITISSUE);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    if(document!=null){
                        try{
                            document.write(outputStream);
                            document.close();
                        }
                        catch (Exception e){
                            return ResponseEntity.ok(new Response("Udalo się utworzyć edycje wydanie, ale wystąpił błąd przy generowaniu dokumentu"));
                        }
                    }
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=WydanieEdit.docx")
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(outputStream.toByteArray());
                }
                else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany kontrahent"));
                }
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany uzytkownik"));
            }
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zle podany uuid Przyjecia"));
        }
    }
    public ResponseEntity<?> deleteIssue(UUID uuid) {
        ProductIssue productIssue = productIssueRepository.findByUuid(uuid).orElse(null);
        if(productIssue!=null){
            productRepository.findByProduct_issue_id(productIssue.getId()).forEach(e->{
                e.setProduct_issue(0);
                e.set_active(true);
                spotRepository.changeState(false,e.getSpot().getId());
            });
            productIssueRepository.delete(productIssue);
            return ResponseEntity.ok(new Response("Usunieto wydanie magazynowe"));
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Zly podany numer przyjecia"));
        }
    }
    public ResponseEntity<?> showAllIssues() {
        List<ProductIssueInfo>  productIssuesInfo = new ArrayList<>();
        productIssueRepository.findAll().forEach(
                e->{
                    productIssuesInfo.add(
                            new ProductIssueInfo(
                                    e.getUuid(),
                                    e.getContractor().getName(),
                                    e.getCreated_at(),
                                    e.getUpdated_at(),
                                    e.getDocument_number()
                            )
                    );
                }
        );
        if(!productIssuesInfo.isEmpty()){
            return ResponseEntity.ok(productIssuesInfo);
        }
        else{
            return ResponseEntity.ok(new Response("Brak wydan"));
        }
    }

    public ResponseEntity<?> showIssueDetails(UUID uuid) {
        ProductIssueDetails productIssueDetails = new ProductIssueDetails();
        ProductIssue productIssue = productIssueRepository.findByUuid(uuid).orElse(null);
        if(productIssue!=null){
            productIssueDetails.setUuid(productIssue.getUuid());
            productIssueDetails.setContractor(productIssue.getContractor().getName());
            productIssueDetails.setUpdated_at(productIssue.getUpdated_at());
            productIssueDetails.setCreated_at(productIssue.getCreated_at());
            productIssueDetails.setDocument_number(productIssue.getDocument_number());
            List<ProductInfoDTO> productsInfo = new ArrayList<>();
            productRepository.findByProduct_issue_id(productIssue.getId()).forEach(
                    e->productsInfo.add(new ProductInfoDTO(
                            e.getUuid(),
                            e.getRfid(),
                            e.getName(),
                            e.getCategory(),
                            e.getSpot(),
                            e.getContractor().getName(),
                            e.getUpdated_at()
                    ))
            );
            productIssueDetails.setProducts(productsInfo);
            return ResponseEntity.ok(productIssueDetails);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Niepoprawne uuid"));
        }
    }
    private void saveHistory(Product product, ActionType actionType, User user){
        ProductHistory productHistory = new ProductHistory();
        productHistory.setProduct(product);
        productHistory.setActionType(actionType);
        productHistory.setUser(user);
        productHistory.setCreated_at(new Timestamp(System.currentTimeMillis()));
        productHistoryRespository.saveAndFlush(productHistory);
    }
    private XWPFDocument getDocumentReceipt(ProductReceipt productReceipt, List<ProductInfoDTO> products, User user, ActionType actionType) {
    DocumentDTO documentDTO = new DocumentDTO(
            productReceipt.getDocument_number(),
            actionType.name(),
            productReceipt.getUpdated_at(),
            productReceipt.getUuid().toString(),
            user,
            productReceipt.getContractor(),
            products
    );
    XWPFDocument documentWord = DocumentWord.createDocument(documentDTO);
    return documentWord;
    }
    private XWPFDocument getDocumentIssue(ProductIssue productIssue, List<ProductInfoDTO> products, User user, ActionType actionType) {
        DocumentDTO documentDTO = new DocumentDTO(
                productIssue.getDocument_number(),
                actionType.name(),
                productIssue.getUpdated_at(),
                productIssue.getUuid().toString(),
                user,
                productIssue.getContractor(),
                products
        );
        XWPFDocument documentWord = DocumentWord.createDocument(documentDTO);
        return documentWord;
    }

}
