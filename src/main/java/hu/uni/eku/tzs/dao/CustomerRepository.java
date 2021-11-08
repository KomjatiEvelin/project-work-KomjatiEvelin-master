package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
}
