package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CustomerRepository;
import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerManagerImpl implements CustomerManager {

    private final CustomerRepository customerRepository;

    private static Customer convertCustomerEntity2Model(CustomerEntity customerEntity) {
        return new Customer(
                customerEntity.getId(),
                customerEntity.getFirstName(),
                customerEntity.getMiddleInitial(),
                customerEntity.getLastName()
        );
    }

    private static CustomerEntity convertCustomerModel2Entity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .middleInitial(customer.getMiddleInitial())
                .lastName(customer.getLastName())
                .build();
    }

    @Override
    public Collection<Customer> readAll() {
        return customerRepository.findAll().stream().map(CustomerManagerImpl::convertCustomerEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Customer readByID(int ID) throws CustomerNotFoundException {
        Optional<CustomerEntity> entity = customerRepository.findById(ID);
        if (entity.isEmpty()) {
            throw new CustomerNotFoundException(String.format("Cannot find customer with ID %s", ID));
        }

        return convertCustomerEntity2Model(entity.get());
    }

    @Override
    public Customer record(Customer customer) throws CustomerAlreadyExistsException {
        if (customerRepository.findById(customer.getId()).isPresent()) {
            throw new CustomerAlreadyExistsException();
        }

        CustomerEntity customerEntity = customerRepository.save(
                CustomerEntity.builder()
                        .id(customer.getId())
                        .firstName(customer.getFirstName())
                        .middleInitial(customer.getMiddleInitial())
                        .lastName(customer.getLastName())
                        .build()
        );
        return convertCustomerEntity2Model(customerEntity);
    }

    @Override
    public Customer modify(Customer customer) throws CustomerNotFoundException {
        CustomerEntity entity = convertCustomerModel2Entity(customer);
        if (customerRepository.findById(customer.getId()).isEmpty()) {
            throw new CustomerNotFoundException();
        }
        return convertCustomerEntity2Model(customerRepository.save(entity));

    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(convertCustomerModel2Entity(customer));
    }
}
