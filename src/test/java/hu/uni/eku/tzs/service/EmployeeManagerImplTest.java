package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.EmployeeRepository;
import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import hu.uni.eku.tzs.model.Employee;
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
class EmployeeManagerImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeManagerImpl service;

    @Test
    void recordEmployeeHappyPath() throws EmployeeAlreadyExistsException {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        EmployeeEntity testEmployeeEntity = TestDataProvider.getJohnDoeEntity();
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        when(employeeRepository.save(any())).thenReturn(testEmployeeEntity);
        // when
        Employee actual = service.record(testEmployee);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testEmployee);
    }

    @Test
    void modifyEmployeeHappyPath() throws EmployeeNotFoundException {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        EmployeeEntity testEmployeeEntity = TestDataProvider.getJohnDoeEntity();
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.of(testEmployeeEntity));
        when(employeeRepository.save(any())).thenReturn(testEmployeeEntity);
        // when
        Employee actual = service.modify(testEmployee);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testEmployee);
    }

    @Test
    void modifyEmployeeThrowsEmployeeNotFoundException() {
        //given
        Employee testEmployee= TestDataProvider.getJohnDoe();
        when(employeeRepository.findById(testEmployee.getId())).thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.modify(testEmployee)).isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void recordEmployeeAlreadyExistsException() {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        EmployeeEntity testEmployeeEntity = TestDataProvider.getJohnDoeEntity();
        when(employeeRepository.findById(TestDataProvider.JOHN_DOE_ID)).thenReturn(Optional.ofNullable(testEmployeeEntity));
        // when
        assertThatThrownBy(() -> service.record(testEmployee)).isInstanceOf(EmployeeAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws EmployeeNotFoundException {
        // given
        when(employeeRepository.findById(TestDataProvider.JOHN_DOE_ID))
            .thenReturn(Optional.of(TestDataProvider.getJohnDoeEntity()));
        Employee expected = TestDataProvider.getJohnDoe();
        // when
        Employee actual = service.readById(TestDataProvider.JOHN_DOE_ID);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdEmployeeNotFoundException() {
        // given
        when(employeeRepository.findById(TestDataProvider.UNKNOWN_ID)).thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.readById(TestDataProvider.UNKNOWN_ID)).isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void readAllHappyPath() {
            // given
            List<EmployeeEntity> employeeEntities = List.of(
                    TestDataProvider.getJaneDoeEntity(),
                    TestDataProvider.getJohnDoeEntity()
            );
            Collection<Employee> expectedEmployees = List.of(
                    TestDataProvider.getJaneDoe(),
                    TestDataProvider.getJohnDoe()
            );
            when(employeeRepository.findAll()).thenReturn(employeeEntities);
            // when
            Collection<Employee> actualEmployees = service.readAll();
            // then
            assertThat(actualEmployees)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedEmployees);
//            .containsExactlyInAnyOrderElementsOf(expectedEmployees);
    }


   /*@Test
    void modifyEmployeeHappyPath() throws EmployeeNotFoundException {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        EmployeeEntity johnDoeEntity = TestDataProvider.getJohnDoeEntity();
        Mockito.lenient().when(employeeRepository.save(johnDoeEntity)).thenReturn(johnDoeEntity);
        // when
        Employee actual = service.modify(testEmployee);
        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(testEmployee);

    }*/


    private static class TestDataProvider {

        public static final int JOHN_DOE_ID=1;

        public static final int JANE_DOE_ID=2;

        public static final int UNKNOWN_ID=12;

        public static Employee getJohnDoe() {
            return new Employee(JOHN_DOE_ID, "John", "J", "Doe");
        }

        public static Employee getJaneDoe() {
            return new Employee(JANE_DOE_ID, "Jane","A", "Doe");
        }

        public static EmployeeEntity getJohnDoeEntity() {
            return EmployeeEntity.builder()
                .id(JOHN_DOE_ID)
                .firstName("John")
                .middleInitial("J")
                .lastName("Doe")
                .build();
        }

        public static EmployeeEntity getJaneDoeEntity() {
            return EmployeeEntity.builder()
                    .id(JANE_DOE_ID)
                    .firstName("Jane")
                    .middleInitial("A")
                    .lastName("Doe")
                    .build();
        }
    }
}