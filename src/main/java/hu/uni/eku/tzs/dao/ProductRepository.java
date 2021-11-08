package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}
