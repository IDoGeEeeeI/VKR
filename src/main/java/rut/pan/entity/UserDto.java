package rut.pan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private Roles role;

    private String name;

    private String caption;

    private String email;


}
