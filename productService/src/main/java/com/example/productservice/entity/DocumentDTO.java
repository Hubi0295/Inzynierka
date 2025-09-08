package com.example.productservice.entity;

import com.example.auth.entity.User;
import com.example.contractorservice.entity.Contractor;
import com.example.product.entity.ProductInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private String documentName;
    private String typ;
    private Timestamp date;
    private String documentUUID;
    private User user;
    private Contractor contractor;
    private List<ProductInfoDTO> products;
}
