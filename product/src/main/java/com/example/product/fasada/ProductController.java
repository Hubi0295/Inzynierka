package com.example.product.fasada;

import com.example.product.entity.*;
import com.example.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @RequestMapping(path = "/",method = RequestMethod.POST)
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO, HttpServletRequest httpServletRequest){
        return productService.addProduct(productCreateDTO, httpServletRequest);
    }
    @RequestMapping(path="/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<?> editProduct(@Valid @RequestBody ProductEditDTO productEditDTO, @PathVariable("uuid") UUID uuid, HttpServletRequest httpServletRequest){
        return productService.editProduct(productEditDTO,uuid, httpServletRequest);
    }
    @RequestMapping(path="/{uuid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteProduct(@PathVariable("uuid") UUID uuid){
        return productService.deleteProduct(uuid);
    }
    @RequestMapping(path="/", method = RequestMethod.GET)
    public ResponseEntity<?> showProducts(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size){
        return productService.showProducts(page,size);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> showProductDetails(@PathVariable("uuid") UUID uuid){
        return productService.showProductDetails(uuid);
    }
    @RequestMapping(value="/{rfid}/location", method = RequestMethod.GET)
    public ResponseEntity<?> getLocationByRFID(@PathVariable("rfid") String rfid){
        return productService.getLocationByRFID(rfid);
    }
    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        return productService.addCategory(categoryDTO);
    }
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<?> getCategories(){
        return productService.getCategories();
    }

    @RequestMapping(path="/transfer", method = RequestMethod.POST)
    public ResponseEntity<?> transfer(HttpServletRequest httpServletRequest,@RequestBody @Valid TrasnferDTO trasnferDTO){
        return productService.transfer(httpServletRequest,trasnferDTO);
    }
    @RequestMapping(path="/inventories/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductsOnShelves(@PathVariable("uuid") UUID uuid){
        return productService.getProductsOnShelves(uuid);
    }
    @RequestMapping(path="/inventories/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> inventory(HttpServletRequest httpServletRequest,@PathVariable("uuid") UUID uuid, @Valid @RequestBody InventoryData inventoryData){
        return productService.inventory(httpServletRequest,uuid, inventoryData);
    }

}
