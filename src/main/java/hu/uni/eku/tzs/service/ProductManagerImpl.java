package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;

import java.util.Collection;

public class ProductManagerImpl implements ProductManager{
    @Override
    public Collection<Product> readAll() {
        return null;
    }

    @Override
    public Product readByID(int ID) throws ProductNotFoundException {
        return null;
    }

    @Override
    public Product record(Product product) throws ProductAlreadyExistsException {
        return null;
    }

    @Override
    public Product modify(Product product) throws ProductNotFoundException {
        return null;
    }

    @Override
    public void delete(Product product) {

    }
}
