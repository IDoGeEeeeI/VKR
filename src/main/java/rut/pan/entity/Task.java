package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @Temporal( TemporalType.TIMESTAMP )
    private Date startDate;

    @Temporal( TemporalType.TIMESTAMP )
    private Date endDate;

    @Temporal( TemporalType.TIMESTAMP )
    private Date startWorkDate;

    @ManyToOne( optional = false )
    private Employer employer;

    private String description;

    @ManyToOne
    private TaskType taskType;
}
