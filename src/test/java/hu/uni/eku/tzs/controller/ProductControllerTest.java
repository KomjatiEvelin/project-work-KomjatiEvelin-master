package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ProductDto;
import hu.uni.eku.tzs.controller.dto.ProductMapper;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.ProductManager;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductManager productManager;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController controller;

    @Test
    void readAllHappyPath(){
        //given
        when(productManager.readAll()).thenReturn(List.of(TestDataProvider.getTestProd()));
        when(productMapper.product2productDto(any())).thenReturn(TestDataProvider.getTestProdDto());
        Collection<ProductDto> expected = List.of(TestDataProvider.getTestProdDto());
        //when
        Collection<ProductDto> actual = controller.readAllProducts();
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdHappyPath() throws ProductNotFoundException {
        // given
        when(productManager.readById(TestDataProvider.ID))
                .thenReturn(TestDataProvider.getTestProd());
        ProductDto expected = TestDataProvider.getTestProdDto();
        when(productMapper.product2productDto(any())).thenReturn(TestDataProvider.getTestProdDto());
        // when
        ProductDto actual = controller.readById(TestDataProvider.ID);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsProductNotFoundException() throws ProductNotFoundException {
        //given
        Product testProduct= TestDataProvider.getTestProd();
        when(productManager.readById(testProduct.getId())).thenThrow(new ProductNotFoundException());
        // when then
        assertThatThrownBy(() -> controller.readById(testProduct.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createProductHappyPath() throws ProductAlreadyExistsException {
        //given
        Product testProd=TestDataProvider.getTestProd();
        ProductDto testProdDto = TestDataProvider.getTestProdDto();
        when(productMapper.productDto2product(testProdDto)).thenReturn(testProd);
        when(productManager.record(testProd)).thenReturn(testProd);
        when(productMapper.product2productDto(testProd)).thenReturn(testProdDto);
        //when
        ProductDto actual=controller.create(testProdDto);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testProdDto);
    }

    @Test
    void createProductThrowsProductAlreadyExistsException() throws ProductAlreadyExistsException {
        //given
        Product testProd=TestDataProvider.getTestProd();
        ProductDto testProdDto = TestDataProvider.getTestProdDto();
        when(productMapper.productDto2product(testProdDto)).thenReturn(testProd);
        when(productManager.record(testProd)).thenThrow(new ProductAlreadyExistsException());
        //when then
        assertThatThrownBy(()->controller.create(testProdDto)).isInstanceOf(ResponseStatusException.class);
    }

   @Test
    void updateHappyPath() throws ProductNotFoundException {
        //given
        ProductDto requestDto=TestDataProvider.getTestProdDto();
        Product testProd=TestDataProvider.getTestProd();
        when(productMapper.productDto2product(requestDto)).thenReturn(testProd);
        when(productManager.modify(testProd)).thenReturn(testProd);
        when(productMapper.product2productDto(testProd)).thenReturn(requestDto);
        ProductDto expected=TestDataProvider.getTestProdDto();
        //when
        ProductDto response = controller.update(requestDto);
        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void updateThrowsProductNotFoundException() throws ProductNotFoundException {
        //given
        Product testProd= TestDataProvider.getTestProd();
        ProductDto testProdDto = TestDataProvider.getTestProdDto();
        when(productMapper.productDto2product(testProdDto)).thenReturn(testProd);
        doThrow(new ProductNotFoundException()).when(productManager).modify(testProd);
        // when then
        assertThatThrownBy(() -> controller.update(testProdDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryHappyPath() throws ProductNotFoundException {
        //given
        Product testProd=TestDataProvider.getTestProd();
        when(productManager.readById(TestDataProvider.ID)).thenReturn(testProd);
        doNothing().when(productManager).delete(testProd);
        //when
        controller.delete(TestDataProvider.ID);
    }

    @Test
    void deleteFromQueryParamWhenProductNotFound() throws ProductNotFoundException {
        //given
        final int notFoundProdId=TestDataProvider.ID;
        doThrow(new ProductNotFoundException()).when(productManager).readById(notFoundProdId);

        //when then
        assertThatThrownBy(()-> controller.delete(notFoundProdId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int ID=1;

        public static Product getTestProd(){
            return new Product(ID,"Test Product",1234);
        }

        public static ProductDto getTestProdDto(){
            return ProductDto.builder()
                    .id(ID)
                    .name("Test Product")
                    .price(1234)
                    .build();
        }
    }
}
