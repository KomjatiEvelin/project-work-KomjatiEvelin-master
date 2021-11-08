package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto customer2customerDto(Customer customer);

    Customer customerDto2customer(CustomerDto customerDto);
}
