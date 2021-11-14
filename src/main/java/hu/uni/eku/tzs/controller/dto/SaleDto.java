package hu.uni.eku.tzs.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    @NotNull(message = "id can not be empty")
    private int salesId;

    @NotNull(message = "employee id required")
    private int salesPersonId;

    @NotNull(message = "customer id required")
    private int customerId;

    @NotNull(message = "product id required")
    private int productId;

    @NotNull(message = "quantity can not be null")
    private int quantity;
}
