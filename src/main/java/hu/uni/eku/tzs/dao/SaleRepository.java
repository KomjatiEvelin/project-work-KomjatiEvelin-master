package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {
}
