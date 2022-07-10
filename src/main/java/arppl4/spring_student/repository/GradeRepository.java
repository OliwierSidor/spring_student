package arppl4.spring_student.repository;

import arppl4.spring_student.model.Grade;
import arppl4.spring_student.model.Student;
import arppl4.spring_student.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Set<Grade> findAllByStudentAndSubject(Student student, Subject subject);
    // find =           SELECT
    // All  =           *
    //                  FROM
    // <Grade, Long> => Grade g
    // By               WHERE
    // Student          g.student_id=
    // And              and
    // Subject          g.subject_id=
}
