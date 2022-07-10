package arppl4.spring_student.service;


import arppl4.spring_student.exception.NotFoundException;
import arppl4.spring_student.model.Grade;
import arppl4.spring_student.model.Student;
import arppl4.spring_student.model.Subject;
import arppl4.spring_student.model.dto.AddGradeRequest;
import arppl4.spring_student.model.dto.GradeDTO;
import arppl4.spring_student.repository.GradeRepository;
import arppl4.spring_student.repository.StudentRepository;
import arppl4.spring_student.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public void addGrade(AddGradeRequest addGradeRequest) {
        Long studentId = addGradeRequest.getStudentId();
        Long subjectId = addGradeRequest.getSubjectId();
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        if (optionalStudent.isPresent() && optionalSubject.isPresent()) {
            Grade grade = new Grade(addGradeRequest, optionalStudent.get(), optionalSubject.get());
            gradeRepository.save(grade);
        } else {
            throw new NotFoundException("Nie ma przedmiotu lub studenta");
        }
    }

    public Set<GradeDTO> listGrades(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            Set<Grade> grades = student.getGradeList();
            Set<GradeDTO> gradesDTO = new HashSet<>();
            for (Grade grade : grades) {
                gradesDTO.add(grade.mapToGradeDTO());
            }
            return gradesDTO;
        }
        return Set.of();
    }

    public Set<GradeDTO> listGrades(Long studentId, Long subjectId) {
        Set<Grade> grades = getGrades(studentId, subjectId);
        Set<GradeDTO> gradesDTO = new HashSet<>();
        for (Grade grade : grades) {
            gradesDTO.add(grade.mapToGradeDTO());
        }
        return gradesDTO;
    }

    public Set<Grade> getGrades(Long studentId, Long subjectId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        if (optionalStudent.isPresent() && optionalSubject.isPresent()) {
            Student student = optionalStudent.get();
            Subject subject = optionalSubject.get();
            Set<Grade> grades = student.getGradeList();
            Set<Grade> gradesForSubject = new HashSet<>();
            for (Grade grade : grades) {
                if (subject.getId().equals(grade.getSubject().getId())) {
                    gradesForSubject.add(grade);
                }
            }
            return gradesForSubject;
        }
        return Set.of();
    }


    public void deleteGrade(Long gradeId) {
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
        if (optionalGrade.isPresent()) {
            Grade grade = optionalGrade.get();
            gradeRepository.delete(grade);
            log.info("Usunięto ocenę: " + grade);
        } else {
            throw new NotFoundException("Nie można usunąć nieistniejącej oceny");
        }
    }

    public void updateGrade(GradeDTO gradeDTO) {
        Long id = gradeDTO.getGradeId();
        Optional<Grade> optionalGrade = gradeRepository.findById(id);
        if (optionalGrade.isPresent()) {
            Grade gradeToUpdate = optionalGrade.get();
            if (gradeDTO.getValue() != null) {
                gradeToUpdate.setValue(gradeDTO.getValue());
            }
            gradeRepository.save(gradeToUpdate);
            log.info("Ocena została poprawiona");
            return;
        }
        throw new EntityNotFoundException("Nie znaleziono oceny o id " + id);
    }

    public GradeDTO getOneGrad(Long gradeId) {
        Optional<Grade> optionalGrade = gradeRepository.findById(gradeId);
        if (optionalGrade.isPresent()) {
            return optionalGrade.get().mapToGradeDTO();
        }
        throw new NotFoundException("Nie znaleziono oceny o id " + gradeId);
    }
}
