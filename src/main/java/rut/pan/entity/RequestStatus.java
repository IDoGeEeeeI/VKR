package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatus {

    @Id
    @GeneratedValue
    private Integer id;

    private String status;

    private String descriptionRu;

    private String description;

}
