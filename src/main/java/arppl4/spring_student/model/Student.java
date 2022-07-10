package arppl4.spring_student.model;

import arppl4.spring_student.model.dto.AddStudentRequest;
import arppl4.spring_student.model.dto.StudentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String indexNumber;

    @OneToMany(mappedBy = "student")
    @EqualsAndHashCode.Exclude
    private Set<Grade> gradeList;

    public StudentDTO mapToStudentDTO() {
        return new StudentDTO(id, name, surname, dateOfBirth, indexNumber);
    }

    public Student(AddStudentRequest addStudentRequest) {
        this.name = addStudentRequest.getName();
        this.surname = addStudentRequest.getSurname();
        this.dateOfBirth = addStudentRequest.getDateOfBirth();
        this.indexNumber = addStudentRequest.getIndexNumber();
    }
}
