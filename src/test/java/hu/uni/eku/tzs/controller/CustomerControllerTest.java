package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.CustomerDto;
import hu.uni.eku.tzs.controller.dto.CustomerMapper;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.CustomerManager;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
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
public class CustomerControllerTest {

    @Mock
    CustomerManager customerManager;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(customerManager.readAll()).thenReturn(List.of(TestDataProvider.getJohnDoe()));
        when(customerMapper.customer2customerDto(any())).thenReturn(TestDataProvider.getJohnDoeDto());
        Collection<CustomerDto> expected = List.of(TestDataProvider.getJohnDoeDto());
        // when
        Collection<CustomerDto> actual = controller.readAllCustomers();
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void readByIdHappyPath() throws CustomerNotFoundException {
        // given
        when(customerManager.readById(TestDataProvider.getJohnDoe().getId()))
                .thenReturn(TestDataProvider.getJohnDoe());
        Customer expected = TestDataProvider.getJohnDoe();
        // when
        Customer actual = customerManager.readById(TestDataProvider.getJohnDoe().getId());
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsCustomerNotFoundException() throws CustomerNotFoundException {
        //given
        Customer testCustomer=TestDataProvider.getJohnDoe();
        when(customerManager.readById(testCustomer.getId())).thenThrow(new CustomerNotFoundException());
        // when then
        assertThatThrownBy(() -> controller.readById(testCustomer.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createCustomerHappyPath() throws CustomerAlreadyExistsException {
        // given
        Customer testCustomer = TestDataProvider.getJohnDoe();
        CustomerDto testCustomerDto = TestDataProvider.getJohnDoeDto();
        when(customerMapper.customerDto2customer(testCustomerDto)).thenReturn(testCustomer);
        when(customerManager.record(testCustomer)).thenReturn(testCustomer);
        when(customerMapper.customer2customerDto(testCustomer)).thenReturn(testCustomerDto);
        // when
        CustomerDto actual = controller.create(testCustomerDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testCustomerDto);
    }

    @Test
    void createCustomerThrowsCustomerAlreadyExistsException() throws CustomerAlreadyExistsException {
        // given
        Customer testCustomer = TestDataProvider.getJohnDoe();
        CustomerDto testCustomerDto = TestDataProvider.getJohnDoeDto();
        when(customerMapper.customerDto2customer(testCustomerDto)).thenReturn(testCustomer);
        when(customerManager.record(testCustomer)).thenThrow(new CustomerAlreadyExistsException());
        // when then
        assertThatThrownBy(() -> controller.create(testCustomerDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws CustomerNotFoundException {
        // given
        CustomerDto requestDto = TestDataProvider.getJohnDoeDto();
        Customer testCustomer = TestDataProvider.getJohnDoe();
        when(customerMapper.customerDto2customer(requestDto)).thenReturn(testCustomer);
        when(customerManager.modify(testCustomer)).thenReturn(testCustomer);
        when(customerMapper.customer2customerDto(testCustomer)).thenReturn(requestDto);
        CustomerDto expected = TestDataProvider.getJohnDoeDto();
        // when
        CustomerDto response = controller.update(requestDto);
        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void updateThrowsCustomerNotFoundException() throws CustomerNotFoundException {
        //given
        Customer testCustomer=TestDataProvider.getJohnDoe();
        CustomerDto testCustomerDto = TestDataProvider.getJohnDoeDto();
        when(customerMapper.customerDto2customer(testCustomerDto)).thenReturn(testCustomer);
        when(customerManager.modify(testCustomer)).thenThrow(new CustomerNotFoundException());
        // when then
        assertThatThrownBy(() -> controller.update(testCustomerDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws CustomerNotFoundException {
        // given
        Customer testCustomer = TestDataProvider.getJohnDoe();
        when(customerManager.readById(TestDataProvider.ID)).thenReturn(testCustomer);
        doNothing().when(customerManager).delete(testCustomer);
        // when
        controller.delete(TestDataProvider.ID);
        // then is not necessary, mock are checked by default
    }

    @Test
    void deleteFromQueryParamWhenCustomerNotFound() throws CustomerNotFoundException {
        // given
        final int notFoundCustomerId = TestDataProvider.ID;
        doThrow(new CustomerNotFoundException()).when(customerManager).readById(notFoundCustomerId);

        // when then
        assertThatThrownBy(() -> controller.delete(notFoundCustomerId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int ID = 1;

        public static Customer getJohnDoe() {
            return new Customer(ID,"John","J","Doe");
        }

        public static CustomerDto getJohnDoeDto() {
            return CustomerDto.builder()
                    .id(ID)
                    .firstName("John")
                    .middleInitial("J")
                    .lastName("Doe")
                    .build();
        }
    }
}
