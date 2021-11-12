package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ProductDto;
import hu.uni.eku.tzs.controller.dto.ProductMapper;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.ProductManager;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "Products")
@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductManager productManager;

    private final ProductMapper productMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<ProductDto> readAllProducts() {
        return productManager.readAll()
                .stream()
                .map(productMapper::product2productDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public ProductDto create(@Valid @RequestBody ProductDto recordRequestDto) {
        Product product = productMapper.productDto2product(recordRequestDto);
        try {
            Product recordedProduct = productManager.record(product);
            return productMapper.product2productDto(recordedProduct);
        } catch (ProductAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public ProductDto update(@Valid @RequestBody ProductDto updateRequestDto) {
        Product product = productMapper.productDto2product(updateRequestDto);
        try {
            Product updatedProduct = productManager.modify(product);
            return productMapper.product2productDto(updatedProduct);
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            productManager.delete(productManager.readById(id));
        } catch (ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"/{id}"})
    public void deleteBasedOnPath(@PathVariable int id) {
        this.delete(id);
    }
}
