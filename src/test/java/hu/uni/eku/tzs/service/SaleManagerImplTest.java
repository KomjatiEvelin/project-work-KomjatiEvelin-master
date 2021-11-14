package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CustomerRepository;
import hu.uni.eku.tzs.dao.EmployeeRepository;
import hu.uni.eku.tzs.dao.ProductRepository;
import hu.uni.eku.tzs.dao.SaleRepository;
import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import hu.uni.eku.tzs.dao.entity.ProductEntity;
import hu.uni.eku.tzs.dao.entity.SaleEntity;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
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
public class SaleManagerImplTest {

    @Mock
    SaleRepository saleRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    SalesManagerImpl service;


    @Test
    void recordSaleHappyPath() throws SaleAlreadyExistsException, CustomerNotFoundException,
            ProductNotFoundException, EmployeeNotFoundException {
        //given
        EmployeeEntity testEmp=TestDataProvider.getTestEmployeeEntity();
        CustomerEntity testCustomer=TestDataProvider.getTestCustomerEntity();
        ProductEntity testProd=TestDataProvider.getTestProdEntity();
        Sale testSale = TestDataProvider.getTestSale1();
        SaleEntity testSaleEntity=TestDataProvider.getTestSaleEntity1();
        when(employeeRepository.findById(testSale.getSalesPersonId())).thenReturn(Optional.ofNullable(testEmp));
        when(customerRepository.findById(testSale.getCustomerId())).thenReturn(Optional.ofNullable(testCustomer));
        when(productRepository.findById(testSale.getProductId())).thenReturn(Optional.ofNullable(testProd));
        when(saleRepository.findById(any())).thenReturn(Optional.empty());
        when(saleRepository.save(any())).thenReturn(testSaleEntity);
        //when
        Sale actual=service.record(testSale);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testSale);
    }

    @Test
    void recordSaleAlreadyExistsException() {
        //given
        Sale testSale=TestDataProvider.getTestSale1();
        SaleEntity testSaleEntity=TestDataProvider.getTestSaleEntity1();
        when(saleRepository.findById(TestDataProvider.TEST_SALE1_ID)).thenReturn(Optional.ofNullable(testSaleEntity));
        //when
        assertThatThrownBy(()->service.record(testSale)).isInstanceOf(SaleAlreadyExistsException.class);
    }

    @Test
    void modifySaleHappyPath() throws SaleNotFoundException, CustomerNotFoundException,
            ProductNotFoundException, EmployeeNotFoundException {
        // given
        EmployeeEntity testEmp=TestDataProvider.getTestEmployeeEntity();
        CustomerEntity testCustomer=TestDataProvider.getTestCustomerEntity();
        ProductEntity testProd=TestDataProvider.getTestProdEntity();
        Sale testSale = TestDataProvider.getTestSale1();
        SaleEntity testSaleEntity = TestDataProvider.getTestSaleEntity1();
        when(employeeRepository.findById(testSale.getSalesPersonId())).thenReturn(Optional.ofNullable(testEmp));
        when(customerRepository.findById(testSale.getCustomerId())).thenReturn(Optional.ofNullable(testCustomer));
        when(productRepository.findById(testSale.getProductId())).thenReturn(Optional.ofNullable(testProd));
        when(saleRepository.findById(testSale.getSalesId())).thenReturn(Optional.of(testSaleEntity));
        when(saleRepository.save(any())).thenReturn(testSaleEntity);
        // when
        Sale actual = service.modify(testSale);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testSale);
    }

    @Test
    void modifySaleThrowsProductNotFoundException() {
        //given
        EmployeeEntity testEmp=TestDataProvider.getTestEmployeeEntity();
        CustomerEntity testCustomer=TestDataProvider.getTestCustomerEntity();
        Sale testSale = TestDataProvider.getTestSale1();
        SaleEntity testSaleEntity = TestDataProvider.getTestSaleEntity1();
        when(employeeRepository.findById(testSale.getSalesPersonId())).thenReturn(Optional.ofNullable(testEmp));
        when(customerRepository.findById(testSale.getCustomerId())).thenReturn(Optional.ofNullable(testCustomer));
        when(productRepository.findById(testSale.getProductId())).thenReturn(Optional.empty());
        when(saleRepository.findById(testSale.getSalesId())).thenReturn(Optional.of(testSaleEntity));
        // when then
        assertThatThrownBy(() -> service.modify(testSale)).isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void modifySaleThrowsEmployeeNotFoundException() {
        //given
        Sale testSale = TestDataProvider.getTestSale1();
        SaleEntity testSaleEntity = TestDataProvider.getTestSaleEntity1();
        when(employeeRepository.findById(testSale.getSalesPersonId())).thenReturn(Optional.empty());
        when(saleRepository.findById(testSale.getSalesId())).thenReturn(Optional.of(testSaleEntity));
        // when then
        assertThatThrownBy(() -> service.modify(testSale)).isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void modifySaleThrowsCustomerNotFoundException() {
        //given
        EmployeeEntity testEmp=TestDataProvider.getTestEmployeeEntity();
        Sale testSale = TestDataProvider.getTestSale1();
        SaleEntity testSaleEntity = TestDataProvider.getTestSaleEntity1();
        when(employeeRepository.findById(testSale.getSalesPersonId())).thenReturn(Optional.ofNullable(testEmp));
        when(customerRepository.findById(testSale.getCustomerId())).thenReturn(Optional.empty());
        when(saleRepository.findById(testSale.getSalesId())).thenReturn(Optional.of(testSaleEntity));
        // when then
        assertThatThrownBy(() -> service.modify(testSale)).isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void readByIdHappyPath() throws SaleNotFoundException {
        //given
        when(saleRepository.findById(TestDataProvider.TEST_SALE1_ID))
                .thenReturn(Optional.of(TestDataProvider.getTestSaleEntity1()));
        Sale expected=TestDataProvider.getTestSale1();
        //when
        Sale actual=service.readById(TestDataProvider.TEST_SALE1_ID);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdSaleNotFoundException() {
        //given
        when(saleRepository.findById(TestDataProvider.UNKNOWN_ID)).thenReturn(Optional.empty());
        //when then
        assertThatThrownBy(()->service.readById(TestDataProvider.UNKNOWN_ID)).isInstanceOf(SaleNotFoundException.class);
    }

    @Test
    void readAllHappyPath() {
        //given
        List<SaleEntity> saleEntities=List.of(
                TestDataProvider.getTestSaleEntity1(),
                TestDataProvider.getTestSaleEntity2()
        );
        Collection<Sale> expectedSales = List.of(
                TestDataProvider.getTestSale1(),
                TestDataProvider.getTestSale2()
        );
        when(saleRepository.findAll()).thenReturn(saleEntities);
        //when
        Collection<Sale> actualSales=service.readAll();
        //then
        assertThat(actualSales)
                .usingRecursiveComparison()
                .isEqualTo(expectedSales);
    }

    private static class TestDataProvider{

        public static final int TEST_SALE1_ID =1;

        public static final int TEST_SALE2_ID =2;

        public static final int UNKNOWN_ID=10;

        public static Customer testCustomer=new Customer(1,
                "John","J","Doe");

        public static Customer testCustomer2=new Customer(2,
                "Jack","A","Smith");

        public static Employee testEmployee=new Employee(1,
                "Jane","A","Doe");

        public static Employee testEmployee2=new Employee(2,
                "Mary","A","Brown");

        public static Product testProd=new Product(1,
                "Test Product",1234);

        public static Product testProd2=new Product(2,
                "Test Product 2",1000);

        public static  Sale getTestSale1(){
            return new Sale(TEST_SALE1_ID,
                    testEmployee.getId(),
                    testCustomer.getId(),
                    testProd.getId(),
                    3);

        }

        public static  Sale getTestSale2(){
            return new Sale(TEST_SALE2_ID,
                    testEmployee2.getId(),
                    testCustomer2.getId(),
                    testProd2.getId(),
                    4);
        }
        public static SaleEntity getTestSaleEntity1(){
            return SaleEntity.builder()
                    .salesId(TEST_SALE1_ID)
                    .salesPersonId(testEmployee.getId())
                    .customerId(testCustomer.getId())
                    .productId(testProd.getId())
                    .quantity(3)
                    .build();
        }

        public static SaleEntity getTestSaleEntity2(){
            return SaleEntity.builder()
                    .salesId(TEST_SALE2_ID)
                    .salesPersonId(testEmployee2.getId())
                    .customerId(testCustomer2.getId())
                    .productId(testProd2.getId())
                    .quantity(4)
                    .build();
        }

        public static CustomerEntity getTestCustomerEntity(){
            return CustomerEntity.builder()
                    .id(1)
                    .firstName("John")
                    .middleInitial("J")
                    .lastName("Doe")
                    .build();
        }

        public static EmployeeEntity getTestEmployeeEntity(){
            return EmployeeEntity.builder()
                    .id(1)
                    .firstName("Jane")
                    .middleInitial("A")
                    .lastName("Doe")
                    .build();
        }

        public  static ProductEntity getTestProdEntity() {
            return ProductEntity.builder()
                    .id(1)
                    .name("Test Product")
                    .price(1234)
                    .build();
        }
    }
}
