package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CustomerRepository;
import hu.uni.eku.tzs.dao.EmployeeRepository;
import hu.uni.eku.tzs.dao.ProductRepository;
import hu.uni.eku.tzs.dao.SaleRepository;
import hu.uni.eku.tzs.dao.entity.SaleEntity;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesManagerImpl implements SalesManager {

    private  final SaleRepository saleRepository;

    private final CustomerRepository customerRepository;

    private  final ProductRepository productRepository;

    private  final EmployeeRepository employeeRepository;

    private static Sale convertSaleEntity2Model(SaleEntity saleEntity) {
        return new Sale(
                saleEntity.getSalesId(),
                saleEntity.getSalesPersonId(),
                saleEntity.getCustomerId(),
                saleEntity.getProductId(),
                saleEntity.getQuantity()
        );
    }

    private static SaleEntity convertSaleModel2Entity(Sale sale) {
        return SaleEntity.builder()
                .salesId(sale.getSalesId())
                .salesPersonId(sale.getSalesPersonId())
                .customerId(sale.getCustomerId())
                .productId(sale.getProductId())
                .quantity(sale.getQuantity())
                .build();
    }

    @Override
    public Collection<Sale> readAll() {
        return saleRepository.findAll().stream().map(SalesManagerImpl::convertSaleEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Sale readById(int id) throws SaleNotFoundException {
        Optional<SaleEntity> entity = saleRepository.findById(id);
        if (entity.isEmpty()) {
            throw  new SaleNotFoundException(String.format("Cannot find sale with ID %s", id));
        }
        return convertSaleEntity2Model(entity.get());
    }

    @Override
    public Sale record(Sale sale) throws SaleAlreadyExistsException, EmployeeNotFoundException,
            CustomerNotFoundException, ProductNotFoundException {

        checkNumberArguments(sale);

        if (saleRepository.findById(sale.getSalesId()).isPresent()) {
            throw new SaleAlreadyExistsException("A sale already exists with this ID");
        }

        checkDependencies(sale);

        SaleEntity saleEntity = saleRepository.save(
                SaleEntity.builder()
                        .salesId(sale.getSalesId())
                        .salesPersonId(sale.getSalesPersonId())
                        .customerId(sale.getCustomerId())
                        .productId(sale.getProductId())
                        .quantity(sale.getQuantity())
                        .build()
        );
        return convertSaleEntity2Model(saleEntity);
    }

    @Override
    public Sale modify(Sale sale) throws SaleNotFoundException, EmployeeNotFoundException, CustomerNotFoundException,
            ProductNotFoundException {

        checkNumberArguments(sale);

        SaleEntity saleEntity = convertSaleModel2Entity(sale);

        if (saleRepository.findById(sale.getSalesId()).isEmpty()) {
            throw new SaleNotFoundException("Cannot find this sale");
        }

        checkDependencies(sale);

        return convertSaleEntity2Model(saleRepository.save(saleEntity));
    }

    @Override
    public void delete(Sale sale) throws SaleNotFoundException {
        if (saleRepository.findById(sale.getSalesId()).isEmpty()) {
            throw new SaleNotFoundException("Cannot find sale with this ID");
        }
        saleRepository.delete(convertSaleModel2Entity(sale));
    }

    private void checkDependencies(Sale sale) throws EmployeeNotFoundException, CustomerNotFoundException,
            ProductNotFoundException {

        if (employeeRepository.findById(sale.getSalesPersonId()).isEmpty()) {
            throw new EmployeeNotFoundException(String.format("Cannot find employee with ID %s",
                    sale.getSalesPersonId()));
        }
        if (customerRepository.findById(sale.getCustomerId()).isEmpty()) {
            throw new CustomerNotFoundException(String.format("Cannot find customer with ID %s", sale.getCustomerId()));
        }

        if (productRepository.findById(sale.getProductId()).isEmpty()) {
            throw new ProductNotFoundException(String.format("Cannot find product with ID %s", sale.getProductId()));
        }
    }

    void checkNumberArguments(Sale sale) {

        if (sale.getSalesId() < 1) {
            throw new IllegalArgumentException("Id can not be smaller than 1");
        }

        if (sale.getQuantity() < 1) {
            throw new IllegalArgumentException("Quantity can not be less than 1");
        }
    }
}
