package com.example.product.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductEditDTO {
    @NotBlank(message = "rfid jest wymagany")
    private String rfid;
    @NotBlank(message = "name jest wymagany")
    private String name;
    @NotNull(message = "category jest wymagany")
    private int category;
    @NotBlank(message = "description jest wymagany")
    private String description;
    @NotNull(message = "weight jest wymagany")
    private float weight;
    @NotNull(message = "height jest wymagany")
    private float height;
    @NotNull(message = "width jest wymagany")
    private float width;
    @NotNull(message = "contractor jest wymagany")
    private int contractor;
}
