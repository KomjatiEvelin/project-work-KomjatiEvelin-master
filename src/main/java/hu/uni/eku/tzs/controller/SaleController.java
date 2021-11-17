package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.SaleDto;
import hu.uni.eku.tzs.controller.dto.SaleMapper;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.SalesManager;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "Sales")
@RequestMapping("/sales")
@RestController
@RequiredArgsConstructor
public class SaleController {

    private final SalesManager salesManager;

    private final SaleMapper saleMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<SaleDto> readAllSales() {
        return salesManager.readAll()
                .stream()
                .map(saleMapper::sale2saleDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("ReadByID")
    @GetMapping(value = {"/{id}"})
    public SaleDto readById(@PathVariable int id) throws SaleNotFoundException {
        try {
            return saleMapper.sale2saleDto(salesManager.readById(id));
        } catch (SaleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public SaleDto create(@Valid @RequestBody SaleDto recordRequestDto) {
        Sale product = saleMapper.saleDto2sale(recordRequestDto);
        try {
            Sale recordedSale = salesManager.record(product);
            return saleMapper.sale2saleDto(recordedSale);
        } catch (SaleAlreadyExistsException | EmployeeNotFoundException
                | CustomerNotFoundException | ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public SaleDto update(@Valid @RequestBody SaleDto updateRequestDto) {
        Sale sale = saleMapper.saleDto2sale(updateRequestDto);
        try {
            Sale updatedSale = salesManager.modify(sale);
            return saleMapper.sale2saleDto(updatedSale);
        } catch (SaleNotFoundException | EmployeeNotFoundException
                | CustomerNotFoundException | ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            salesManager.delete(salesManager.readById(id));
        } catch (SaleNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"/{id}"})
    public void deleteBasedOnPath(@PathVariable int id) {
        this.delete(id);
    }
}
