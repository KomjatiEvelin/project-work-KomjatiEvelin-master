package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto product2productDto(Product product);

    Product productDto2product(ProductDto productDto);
}
