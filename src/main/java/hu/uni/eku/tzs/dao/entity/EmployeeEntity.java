package hu.uni.eku.tzs.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Employees")
public class EmployeeEntity {

    @Id
    @Column(name = "EmployeeID")
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "MiddleInitial")
    private String middleInitial;

    @Column(name = "LastName")
    private String lastName;
}
