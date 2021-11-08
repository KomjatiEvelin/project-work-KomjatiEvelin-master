package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;

import java.util.Collection;

public interface EmployeeManager {

    Collection<Employee> readAll();

    Employee readById(int id) throws EmployeeNotFoundException;

    Employee record(Employee employee) throws EmployeeAlreadyExistsException;

    Employee modify(Employee employee) throws  EmployeeNotFoundException;

    void delete(Employee employee);

}
