package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Customers")
public class CustomerEntity {
    @Id
    @Column(name = "CustomerID")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "MiddleInitial")
    private String middleInitial;

    @Column(name = "LastName")
    private String lastName;
}
