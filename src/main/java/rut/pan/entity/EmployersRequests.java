package rut.pan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class EmployersRequests {

    @Id
    @GeneratedValue
    private Integer id;

    private String requestName;

    private String requestDescription;

    @ManyToOne
    @JoinColumn(name="requesting_employer_id")
    private Employer requestingEmployer;

    @ManyToOne
    @JoinColumn(name="supervisor_id")
    private Employer supervisor;

    @ManyToOne
    private RequestStatus requestStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    public boolean isEmpty() {
        return (requestName == null || requestName.isEmpty()) &&
                (requestDescription == null || requestDescription.isEmpty()) &&
                requestingEmployer == null &&
                supervisor == null &&
                requestStatus == null &&
                startDate == null &&
                endDate == null;
    }
}
