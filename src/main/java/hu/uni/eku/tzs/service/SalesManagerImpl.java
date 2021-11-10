package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.SaleRepository;
import hu.uni.eku.tzs.dao.entity.SaleEntity;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesManagerImpl implements SalesManager {

    private  final SaleRepository saleRepository;

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
    public Sale record(Sale sale) throws SaleAlreadyExistsException {
        if (saleRepository.findById(sale.getSalesId()).isPresent()) {
            throw new SaleAlreadyExistsException();
        }

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
    public Sale modify(Sale sale) throws SaleNotFoundException {
        SaleEntity saleEntity = convertSaleModel2Entity(sale);
        if (saleRepository.findById(sale.getSalesId()).isEmpty()) {
            throw new SaleNotFoundException("Cannot find this sale");
        }
        return convertSaleEntity2Model(saleRepository.save(saleEntity));
    }

    @Override
    public void delete(Sale sale) throws SaleNotFoundException {
        saleRepository.delete(convertSaleModel2Entity(sale));
    }
}
