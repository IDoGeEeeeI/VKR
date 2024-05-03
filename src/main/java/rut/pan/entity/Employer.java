package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employer {

    @Id
    @NonNull
    @GeneratedValue
    private Integer id;

    private String name;

    private String caption;

    private String email;


}
