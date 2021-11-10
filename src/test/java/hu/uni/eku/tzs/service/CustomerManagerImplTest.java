package hu.uni.eku.tzs.service;


import hu.uni.eku.tzs.dao.CustomerRepository;
import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
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
public class CustomerManagerImplTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerManagerImpl service;

    @Test
    void recordCustomerHappyPath() throws CustomerAlreadyExistsException {
        // given
        Customer testCustomer = TestDataProvider.getJohnDoe();
        CustomerEntity testCustomerEntity = TestDataProvider.getJohnDoeEntity();
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(testCustomerEntity);
        // when
        Customer actual = service.record(testCustomer);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testCustomer);
    }

    @Test
    void recordCustomerAlreadyExistsException() {
        // given
        Customer testCustomer = TestDataProvider.getJohnDoe();
        CustomerEntity testCustomerEntity = TestDataProvider.getJohnDoeEntity();
        when(customerRepository.findById(TestDataProvider.JOHN_DOE_ID)).thenReturn(Optional.ofNullable(testCustomerEntity));
        // when
        assertThatThrownBy(() -> service.record(testCustomer)).isInstanceOf(CustomerAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws CustomerNotFoundException {
        // given
        when(customerRepository.findById(TestDataProvider.JOHN_DOE_ID))
                .thenReturn(Optional.of(TestDataProvider.getJohnDoeEntity()));
        Customer expected = TestDataProvider.getJohnDoe();
        // when
        Customer actual = service.readById(TestDataProvider.JOHN_DOE_ID);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdCustomerNotFoundException() {
        // given
        when(customerRepository.findById(TestDataProvider.UNKNOWN_ID)).thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.readById(TestDataProvider.UNKNOWN_ID)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void readAllHappyPath() {
        // given
        List<CustomerEntity> customerEntities = List.of(
                TestDataProvider.getJaneDoeEntity(),
                TestDataProvider.getJohnDoeEntity()
        );
        Collection<Customer> expectedCustomers = List.of(
                TestDataProvider.getJaneDoe(),
                TestDataProvider.getJohnDoe()
        );
        when(customerRepository.findAll()).thenReturn(customerEntities);
        // when
        Collection<Customer> actualEmployees = service.readAll();
        // then
        assertThat(actualEmployees)
                .usingRecursiveComparison()
                .isEqualTo(expectedCustomers);
//            .containsExactlyInAnyOrderElementsOf(expectedCustomers);
    }


    private static class TestDataProvider {

        public static final int JOHN_DOE_ID=1;

        public static final int JANE_DOE_ID=2;

        public static final int UNKNOWN_ID=12;

        public static Customer getJohnDoe() {
            return new Customer(JOHN_DOE_ID, "John", "J", "Doe");
        }

        public static Customer getJaneDoe() {
            return new Customer(JANE_DOE_ID, "Jane","A", "Doe");
        }

        public static CustomerEntity getJohnDoeEntity() {
            return CustomerEntity.builder()
                    .id(JOHN_DOE_ID)
                    .firstName("John")
                    .middleInitial("J")
                    .lastName("Doe")
                    .build();
        }

        public static CustomerEntity getJaneDoeEntity() {
            return CustomerEntity.builder()
                    .id(JANE_DOE_ID)
                    .firstName("Jane")
                    .middleInitial("A")
                    .lastName("Doe")
                    .build();
        }
    }
}
