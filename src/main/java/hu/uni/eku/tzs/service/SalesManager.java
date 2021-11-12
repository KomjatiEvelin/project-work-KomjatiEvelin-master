package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;

import java.util.Collection;

public interface SalesManager {
    Collection<Sale> readAll();

    Sale readById(int id) throws SaleNotFoundException;

    Sale record(Sale sale) throws SaleAlreadyExistsException,
            EmployeeNotFoundException, CustomerNotFoundException, ProductNotFoundException;

    Sale modify(Sale sale) throws SaleNotFoundException, EmployeeNotFoundException, CustomerNotFoundException, ProductNotFoundException;

    void delete(Sale sale) throws SaleNotFoundException;
}
