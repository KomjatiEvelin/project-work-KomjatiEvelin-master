package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ProductRepository;
import hu.uni.eku.tzs.dao.entity.ProductEntity;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductManagerImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductManagerImpl service;

    @Test
    void recordProductHappyPath() throws ProductAlreadyExistsException {
        //given
        Product testProd=TestDataProvider.getTestProd1();
        ProductEntity testProdEntity=TestDataProvider.getTestProd1Entity();
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        when(productRepository.save(any())).thenReturn(testProdEntity);
        //when
        Product actual=service.record(testProd);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testProd);
    }

    @Test
    void recordProductAlreadyExistsException() {
        //given
        Product testProd=TestDataProvider.getTestProd1();
        ProductEntity testProdEntity=TestDataProvider.getTestProd1Entity();
        when(productRepository.findById(TestDataProvider.PROD1_ID)).thenReturn(Optional.ofNullable(testProdEntity));
        //when
        assertThatThrownBy(()->service.record(testProd)).isInstanceOf(ProductAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws ProductNotFoundException {
        //given
        when(productRepository.findById(TestDataProvider.PROD1_ID))
                .thenReturn(Optional.of(TestDataProvider.getTestProd1Entity()));
        Product expected=TestDataProvider.getTestProd1();
        //when
        Product actual=service.readById(TestDataProvider.PROD1_ID);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdProductNotFoundException() {
        //given
        when(productRepository.findById(TestDataProvider.UNKNOWN_ID)).thenReturn(Optional.empty());
        //when then
        assertThatThrownBy(()->service.readById(TestDataProvider.UNKNOWN_ID)).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void readAllHappyPath() {
        //given
        List<ProductEntity> productEntities=List.of(
                TestDataProvider.getTestProd1Entity(),
                TestDataProvider.getTestProd2Entity()
        );
        Collection<Product> expectedProducts=List.of(
                TestDataProvider.getTestProd1(),
                TestDataProvider.getTestProd2()
        );
        when(productRepository.findAll()).thenReturn(productEntities);
        //when
        Collection<Product> actualProducts=service.readAll();
        //then
        assertThat(actualProducts)
                .usingRecursiveComparison()
                .isEqualTo(expectedProducts);
    }

    private static class TestDataProvider {

        public static final int PROD1_ID=1;

        public static final int PROD2_ID=2;

        public static final int UNKNOWN_ID=23;

        public static  Product getTestProd1() {
            return  new Product(PROD1_ID,"Test Product 1",1230);
        }

        public  static ProductEntity getTestProd1Entity() {
            return  ProductEntity.builder()
                    .id(PROD1_ID)
                    .name("Test Product 1")
                    .price(1230)
                    .build();
        }

        public static  Product getTestProd2() {
            return  new Product(PROD2_ID,"Test Product 2",1030);
        }

        public  static ProductEntity getTestProd2Entity() {
            return  ProductEntity.builder()
                    .id(PROD2_ID)
                    .name("Test Product 2")
                    .price(1030)
                    .build();
        }
    }
}
