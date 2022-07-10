package arppl4.spring_student.model;

import arppl4.spring_student.model.dto.AddGradeRequest;
import arppl4.spring_student.model.dto.GradeDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @GeneratedValue
    private Long id;

    private Integer value;

    @CreationTimestamp
    private LocalDateTime dateAdded;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Subject subject;
    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Student student;

    public GradeDTO mapToGradeDTO() {
        return new GradeDTO(id, value, student.getId(), subject.getId());
    }

    public Grade(Integer value, Student student, Subject subject) {
        this.value = value;
        this.student = student;
        this.subject = subject;
    }

}
