package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.SaleDto;
import hu.uni.eku.tzs.controller.dto.SaleMapper;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.SalesManager;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
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
public class SaleControllerTest {

    @Mock
    private SalesManager salesManager;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleController controller;

    @Test
    void readAllHappyPath() {
        //given
        when(salesManager.readAll()).thenReturn(List.of(TestDataProvider.getTestSale()));
        when(saleMapper.sale2saleDto(any())).thenReturn(TestDataProvider.getTestSaleDto());
        Collection<SaleDto> expected=List.of(TestDataProvider.getTestSaleDto());
        //when
        Collection<SaleDto> actual= controller.readAllSales();
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void updateHappyPath() throws SaleNotFoundException, CustomerNotFoundException,
            ProductNotFoundException, EmployeeNotFoundException {
        //given
        SaleDto requestDto= TestDataProvider.getTestSaleDto();
        Sale testSale= TestDataProvider.getTestSale();
        when(saleMapper.saleDto2sale(requestDto)).thenReturn(testSale);
        when(salesManager.modify(testSale)).thenReturn(testSale);
        when(saleMapper.sale2saleDto(testSale)).thenReturn(requestDto);
        SaleDto expected= TestDataProvider.getTestSaleDto();
        //when
        SaleDto response = controller.update(requestDto);
        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void updateThrowsProductNotFoundException() throws ProductNotFoundException, SaleNotFoundException,
            CustomerNotFoundException, EmployeeNotFoundException {
        //given
        Sale testSale = TestDataProvider.getTestSale();
        SaleDto testSaleDto = TestDataProvider.getTestSaleDto();
        when(saleMapper.saleDto2sale(testSaleDto)).thenReturn(testSale);
        when(salesManager.modify(testSale)).thenThrow(new ProductNotFoundException());
        // when then
        assertThatThrownBy(() -> controller.update(testSaleDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryHappyPath() throws SaleNotFoundException {
        //given
        Sale testSale= TestDataProvider.getTestSale();
        when(salesManager.readById(TestDataProvider.SALE_ID)).thenReturn(testSale);
        doNothing().when(salesManager).delete(testSale);
        //when
        controller.delete(TestDataProvider.SALE_ID);
    }

    @Test
    void deleteFromQueryParamWhenProductNotFound() throws SaleNotFoundException {
        //given
        final int notFoundSaleId= TestDataProvider.SALE_ID;
        doThrow(new SaleNotFoundException()).when(salesManager).readById(notFoundSaleId);

        //when then
        assertThatThrownBy(()-> controller.delete(notFoundSaleId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int SALE_ID=1;

        public static final int SALESMAN_ID=1;

        public static final int CUSTOMER_ID=1;

        public static final int PRODUCT_ID=1;

        public static final int QUANTITY=2;

        public static Sale getTestSale(){
            return new Sale(SALE_ID,SALESMAN_ID,CUSTOMER_ID,PRODUCT_ID,QUANTITY);        }

        public static SaleDto getTestSaleDto(){
            return SaleDto.builder()
                    .salesPersonId(SALESMAN_ID)
                    .customerId(CUSTOMER_ID)
                    .productId(PRODUCT_ID)
                    .quantity(QUANTITY)
                    .build();
        }
    }
}
