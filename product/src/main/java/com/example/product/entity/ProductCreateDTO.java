package com.example.product.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDTO extends ProductEditDTO{
    @NotNull(message = "spot jest wymagany")
    private Integer spot;
}
