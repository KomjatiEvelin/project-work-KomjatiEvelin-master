package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sales")
public class SaleEntity {

    @Id
    private int salesId;

    @Column(name = "salesperson_id")
    private int salesPersonId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "quantity")
    private int quantity;
}
