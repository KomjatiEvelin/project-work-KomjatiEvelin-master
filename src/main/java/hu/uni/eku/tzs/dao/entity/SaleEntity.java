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

    @Column(name = "SalesPersonID")
    private int salesPersonId;

    @Column(name = "CustomerID")
    private int customerId;

    @Column(name = "ProductID")
    private int productId;

    @Column(name = "Quantity")
    private int quantity;
}
