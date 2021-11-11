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

    @NotNull
    private int salesId;

    @NotNull
    private int salesPersonId;

    @NotNull
    private int customerId;

    @NotNull
    private int productId;

    @NotNull
    private int quantity;
}
