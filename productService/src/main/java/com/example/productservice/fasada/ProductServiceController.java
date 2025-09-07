package com.example.productservice.fasada;

import com.example.auth.entity.Response;
import com.example.productservice.entity.ProductIssueDTO;
import com.example.productservice.entity.ProductReceiptDTO;
import com.example.productservice.service.ProductServiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/productService")
@AllArgsConstructor
public class ProductServiceController {
    private final ProductServiceService productServiceService;
    @RequestMapping(path="/receipts",method = RequestMethod.POST)
    public ResponseEntity<?> createProductReceipt(@Valid @RequestBody ProductReceiptDTO productReceiptDTO, HttpServletRequest httpServletRequest){
        return productServiceService.createProductReceipt(productReceiptDTO,httpServletRequest);
    }
    @RequestMapping(path="/receipts/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> editReceipt(@Valid @RequestBody ProductReceiptDTO productReceiptDTO, HttpServletRequest httpServletRequest, @PathVariable("uuid")UUID uuid){
        return productServiceService.editReceipt(productReceiptDTO,httpServletRequest,uuid);
    }
    @RequestMapping(path="/receipts/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteReceipt(@PathVariable("uuid") UUID uuid){
        return productServiceService.deleteReceipt(uuid);
    }
    @RequestMapping(path="/receipts", method = RequestMethod.GET)
    public ResponseEntity<?> showAllReceipts(){
        return productServiceService.showAllReceipts();
    }
    @RequestMapping(path="/receipts/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> showReceiptDetails(@PathVariable("uuid") UUID uuid){
        return productServiceService.showReceiptDetails(uuid);
    }



    @RequestMapping(path="/issues",method = RequestMethod.POST)
    public ResponseEntity<?> createProductIssue(@Valid @RequestBody ProductIssueDTO productissueDTO, HttpServletRequest httpServletRequest){
        return productServiceService.createProductIssue(productissueDTO,httpServletRequest);
    }
    @RequestMapping(path="/issues/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> editIssue(@Valid @RequestBody ProductIssueDTO productissueDTO, HttpServletRequest httpServletRequest, @PathVariable("uuid")UUID uuid){
        return productServiceService.editIssue(productissueDTO,httpServletRequest,uuid);
    }
    @RequestMapping(path="/issues/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteIssue(@PathVariable("uuid") UUID uuid){
        return productServiceService.deleteIssue(uuid);
    }
    @RequestMapping(path="/issues", method = RequestMethod.GET)
    public ResponseEntity<?> showAllIssues(){
        return productServiceService.showAllIssues();
    }
    @RequestMapping(path="/issues/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> showIssueDetails(@PathVariable("uuid") UUID uuid){
        return productServiceService.showIssueDetails(uuid);
    }



}
