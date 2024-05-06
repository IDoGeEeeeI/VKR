package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_dto", uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})})
public class UserDto implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    private String login;

    private String password;

    @ManyToOne
    private Roles role;


}
