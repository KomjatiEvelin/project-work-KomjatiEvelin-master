package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;

import java.util.Collection;

public interface CustomerManager {

    Collection<Customer> readAll();

    Customer readById(int id) throws CustomerNotFoundException;

    Customer record(Customer customer) throws CustomerAlreadyExistsException;

    Customer modify(Customer customer) throws CustomerNotFoundException;

    void delete(Customer customer) throws CustomerNotFoundException;
}
