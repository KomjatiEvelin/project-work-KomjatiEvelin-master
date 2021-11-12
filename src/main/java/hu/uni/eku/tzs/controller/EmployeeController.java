package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.EmployeeDto;
import hu.uni.eku.tzs.controller.dto.EmployeeMapper;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.EmployeeManager;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api(tags = "Employees")
@RequestMapping("/employees")
@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeManager employeeManager;

    private final EmployeeMapper employeeMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<EmployeeDto> readAllEmployees() {
        return employeeManager.readAll()
            .stream()
            .map(employeeMapper::employee2employeeDto)
            .collect(Collectors.toList());

    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public EmployeeDto create(@Valid @RequestBody EmployeeDto recordRequestDto) {
        Employee employee = employeeMapper.employeeDto2employee(recordRequestDto);
        try {
            Employee recordedEmployee = employeeManager.record(employee);
            return employeeMapper.employee2employeeDto(recordedEmployee);
        } catch (EmployeeAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public EmployeeDto update(@Valid @RequestBody EmployeeDto updateRequestDto) {
        Employee employee = employeeMapper.employeeDto2employee(updateRequestDto);
        try {
            Employee updatedEmployee = employeeManager.modify(employee);
            return employeeMapper.employee2employeeDto(updatedEmployee);
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            employeeManager.delete(employeeManager.readById(id));
        } catch (EmployeeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"/{id}"})
    public void deleteBasedOnPath(@PathVariable int id) {
        this.delete(id);
    }

}
