package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
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

    @ManyToOne( optional = false )
    private Employer creator;

    private String description;

    @ManyToOne
    private TaskType taskType;

    @ManyToOne
    private Status status;

    @ManyToOne
    private Prioritize prioritizer;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

}
