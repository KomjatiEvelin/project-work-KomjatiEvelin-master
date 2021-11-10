package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;

import java.util.Collection;

public interface SalesManager {
    Collection<Sale> readAll();

    Sale readById(int id) throws SaleNotFoundException;

    Sale record(Sale sale) throws SaleAlreadyExistsException;

    Sale modify(Sale sale) throws SaleNotFoundException;

    void delete(Sale sale) throws SaleNotFoundException;
}
