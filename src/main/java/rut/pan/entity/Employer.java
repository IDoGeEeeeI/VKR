package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employer {

    @Id
    @NonNull
    @GeneratedValue
    private Integer id;

    private String name;

    private String caption;

    private String email;

    @OneToOne
    private UserDto user;

    @ManyToOne
    private Employer supervisor;

    @OneToMany(mappedBy = "supervisor")
    private List<Employer> employers;

}
