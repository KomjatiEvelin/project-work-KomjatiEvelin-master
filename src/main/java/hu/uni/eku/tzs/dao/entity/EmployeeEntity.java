package hu.uni.eku.tzs.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "employees")
public class EmployeeEntity {

    @Id
    private int id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "MiddleInitial")
    private String middleInitial;

    @Column(name = "LastName")
    private String lastName;
}
