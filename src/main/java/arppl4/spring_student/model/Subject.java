package arppl4.spring_student.model;

import arppl4.spring_student.model.dto.AddSubjectRequest;
import arppl4.spring_student.model.dto.SubjectDTO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue
    private Long id;

    private String subjectName;

    @OneToMany(mappedBy = "subject")
    @ToString.Exclude
    private Set<Grade> gradeList;

    public SubjectDTO mapToSubjectDTO(){
        return new SubjectDTO(id, subjectName);
    }

//    public Subject(AddSubjectRequest addSubjectRequest){
//        this.subjectName = addSubjectRequest.getSubjectName();
//    }

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }
}
