package rut.pan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String login;

    private String password;

    @OneToOne
    private Employer employer;

    @ManyToOne
    private Roles role;


}
