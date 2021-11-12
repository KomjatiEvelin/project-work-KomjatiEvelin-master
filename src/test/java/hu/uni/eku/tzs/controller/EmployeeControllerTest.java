package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.EmployeeDto;
import hu.uni.eku.tzs.controller.dto.EmployeeMapper;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.EmployeeManager;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
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
class EmployeeControllerTest {

    @Mock
    private EmployeeManager employeeManager;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(employeeManager.readAll()).thenReturn(List.of(TestDataProvider.getJohnDoe()));
        when(employeeMapper.employee2employeeDto(any())).thenReturn(TestDataProvider.getJohnDoeDto());
        Collection<EmployeeDto> expected = List.of(TestDataProvider.getJohnDoeDto());
        // when
        Collection<EmployeeDto> actual = controller.readAllEmployees();
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void createEmployeeHappyPath() throws EmployeeAlreadyExistsException {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        EmployeeDto testEmployeeDto = TestDataProvider.getJohnDoeDto();
        when(employeeMapper.employeeDto2employee(testEmployeeDto)).thenReturn(testEmployee);
        when(employeeManager.record(testEmployee)).thenReturn(testEmployee);
        when(employeeMapper.employee2employeeDto(testEmployee)).thenReturn(testEmployeeDto);
        // when
        EmployeeDto actual = controller.create(testEmployeeDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testEmployeeDto);
    }

    @Test
    void createEmployeeThrowsEmployeeAlreadyExistsException() throws EmployeeAlreadyExistsException {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        EmployeeDto testEmployeeDto = TestDataProvider.getJohnDoeDto();
        when(employeeMapper.employeeDto2employee(testEmployeeDto)).thenReturn(testEmployee);
        when(employeeManager.record(testEmployee)).thenThrow(new EmployeeAlreadyExistsException());
        // when then
        assertThatThrownBy(() -> controller.create(testEmployeeDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws EmployeeNotFoundException {
        // given
        EmployeeDto requestDto = TestDataProvider.getJohnDoeDto();
        Employee testEmployee = TestDataProvider.getJohnDoe();
        when(employeeMapper.employeeDto2employee(requestDto)).thenReturn(testEmployee);
        when(employeeManager.modify(testEmployee)).thenReturn(testEmployee);
        when(employeeMapper.employee2employeeDto(testEmployee)).thenReturn(requestDto);
        EmployeeDto expected = TestDataProvider.getJohnDoeDto();
        // when
        EmployeeDto response = controller.update(requestDto);
        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void updateThrowsEmployeeNotFoundException() throws EmployeeNotFoundException {
        //given
        Employee testEmployee= TestDataProvider.getJohnDoe();
        EmployeeDto testEmployeeDto = TestDataProvider.getJohnDoeDto();
        when(employeeMapper.employeeDto2employee(testEmployeeDto)).thenReturn(testEmployee);
        when(employeeManager.modify(testEmployee)).thenThrow(new EmployeeNotFoundException());
        // when then
        assertThatThrownBy(() -> controller.update(testEmployeeDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws EmployeeNotFoundException {
        // given
        Employee testEmployee = TestDataProvider.getJohnDoe();
        when(employeeManager.readById(TestDataProvider.ID)).thenReturn(testEmployee);
        doNothing().when(employeeManager).delete(testEmployee);
        // when
        controller.delete(TestDataProvider.ID);
        // then is not necessary, mock are checked by default
    }

    @Test
    void deleteFromQueryParamWhenEmployeeNotFound() throws EmployeeNotFoundException {
        // given
        final int notFoundEmployeeId = TestDataProvider.ID;
        doThrow(new EmployeeNotFoundException()).when(employeeManager).readById(notFoundEmployeeId);

        // when then
        assertThatThrownBy(() -> controller.delete(notFoundEmployeeId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int ID = 1;


        public static Employee getJohnDoe() {
            return new Employee(ID, "John", "J", "Doe");
        }

        public static EmployeeDto getJohnDoeDto() {
            return EmployeeDto.builder()
                    .id(ID)
                    .firstName("John")
                    .middleInitial("J")
                    .lastName("Doe")
                    .build();
        }
    }


}