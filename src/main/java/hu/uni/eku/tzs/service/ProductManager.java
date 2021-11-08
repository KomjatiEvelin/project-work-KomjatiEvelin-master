package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;

import java.util.Collection;

public interface ProductManager {

    Collection<Product> readAll();

    Product readById(int id) throws ProductNotFoundException;

    Product record(Product product) throws ProductAlreadyExistsException;

    Product modify(Product product) throws  ProductNotFoundException;

    void delete(Product product);
}
