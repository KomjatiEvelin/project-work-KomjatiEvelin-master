package hu.uni.eku.tzs.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "price cannot be empty")
    private double price;
}
