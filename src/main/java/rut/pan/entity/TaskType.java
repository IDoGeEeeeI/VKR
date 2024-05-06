package rut.pan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TaskType {

    @Id
    @GeneratedValue
    private Long id;

    private String type;

}
