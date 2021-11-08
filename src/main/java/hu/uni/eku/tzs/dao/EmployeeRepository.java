package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
}
