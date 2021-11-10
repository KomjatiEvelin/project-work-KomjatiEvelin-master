package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ProductRepository;
import hu.uni.eku.tzs.dao.entity.ProductEntity;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductManagerImpl implements ProductManager {

    private final ProductRepository productRepository;

    private static Product convertProductEntity2Model(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice()
        );
    }

    private static ProductEntity convertProductModel2Entity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
    @Override
    public Collection<Product> readAll() {
        return productRepository.findAll().stream().map(ProductManagerImpl::convertProductEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Product readById(int id) throws ProductNotFoundException {
        Optional<ProductEntity> entity=productRepository.findById(id);
        if(entity.isEmpty()){
            throw new ProductNotFoundException(String.format("Cannot find product with ID %s", id));
        }
        return convertProductEntity2Model(entity.get());
    }

    @Override
    public Product record(Product product) throws ProductAlreadyExistsException {
        if(productRepository.findById(product.getId()).isPresent()){
            throw new ProductAlreadyExistsException();
        }

        ProductEntity productEntity=productRepository.save(
                ProductEntity.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .build()
        );
        return convertProductEntity2Model(productEntity);
    }

    @Override
    public Product modify(Product product) throws ProductNotFoundException {
        ProductEntity productEntity=convertProductModel2Entity(product);
        if(productRepository.findById(product.getId()).isEmpty()){
            throw new ProductNotFoundException(String.format("Cannot find product with ID %s", product.getId()));
        }

        return convertProductEntity2Model(productRepository.save(productEntity));
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(convertProductModel2Entity(product));
    }
}
