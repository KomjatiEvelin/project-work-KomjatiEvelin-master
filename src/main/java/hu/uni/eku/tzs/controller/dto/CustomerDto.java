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
public class CustomerDto {

    private int id;

    @NotBlank(message = "first name cannot be empty")
    private String firstName;

    private String middleInitial;

    @NotBlank(message = "last name cannot be empty")
    private String lastName;
}
