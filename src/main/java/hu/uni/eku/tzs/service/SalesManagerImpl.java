package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;

import java.util.Collection;

public class SalesManagerImpl implements SalesManager{
    @Override
    public Collection<Sale> readAll() {
        return null;
    }

    @Override
    public Collection<Sale> readByID(int id) {
        return null;
    }

    @Override
    public Sale record(Sale sale) throws SaleAlreadyExistsException {
        return null;
    }

    @Override
    public Sale modify(Sale sale) throws SaleNotFoundException {
        return null;
    }

    @Override
    public void delete(Sale sale) throws SaleNotFoundException {

    }
}
