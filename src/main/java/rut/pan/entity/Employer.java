package rut.pan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Employer {

    @Id
    @GeneratedValue
    private Integer id;

    private String employerName;

}
