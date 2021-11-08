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
public class SaleDto {

    private int salesId;

    @NotBlank(message = "Employee id cannot be empty")
    private int salesPersonId;

    @NotBlank(message = "Customer id cannot be empty")
    private int customerId;

    @NotBlank(message = "Product id cannot be empty")
    private int productId;

    @NotBlank(message = "Quantity cannot be empty")
    private int quantity;
}
