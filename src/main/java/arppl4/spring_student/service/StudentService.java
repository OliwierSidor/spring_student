package arppl4.spring_student.service;

import arppl4.spring_student.exception.NotFoundException;
import arppl4.spring_student.model.Grade;
import arppl4.spring_student.model.Student;
import arppl4.spring_student.model.Subject;
import arppl4.spring_student.model.dto.AddStudentRequest;
import arppl4.spring_student.model.dto.GradeDTO;
import arppl4.spring_student.model.dto.StudentAverageCheckResponse;
import arppl4.spring_student.model.dto.StudentDTO;
import arppl4.spring_student.repository.GradeRepository;
import arppl4.spring_student.repository.StudentRepository;
import arppl4.spring_student.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final GradeService gradeService;
    private final SubjectRepository subjectRepository;

    public List<StudentDTO> findAllStudents() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        for (Student student : studentList) {
            studentDTOS.add(student.mapToStudentDTO());
        }
        return studentDTOS;
    }

    public void addStudent(AddStudentRequest studentDTO) {
        Student newStudent = new Student(studentDTO);
        studentRepository.save(newStudent);
    }

    public void updateStudent(StudentDTO studentDTO) {
        Long id = studentDTO.getId();
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student studentToUpdate = optionalStudent.get();
            if (studentDTO.getName() != null) {
                studentToUpdate.setName(studentDTO.getName());
            }
            if (studentDTO.getSurname() != null) {
                studentToUpdate.setSurname(studentDTO.getSurname());
            }
            if (studentDTO.getDateOfBirth() != null) {
                studentToUpdate.setDateOfBirth(studentDTO.getDateOfBirth());
            }
            studentRepository.save(studentToUpdate);
            log.info("Student został zaktualizowany");
            return;
        }
        throw new EntityNotFoundException("Nie znaleziono studenta o id: " + id);
    }

    public void deleteStudent(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getGradeList().isEmpty()) {
                studentRepository.delete(student);
                log.info("Usunięto studenta: " + student);
            } else {
                log.info("Nie można usunąć " + student + " bo ma oceny");
            }
        } else {
            throw new EntityNotFoundException("Nie znaleziono studenta o id: " + id);
        }
    }

    public void addStudents(List<AddStudentRequest> list) {
        for (AddStudentRequest addStudentRequest : list) {
            addStudent(addStudentRequest);
        }
    }

    public StudentDTO findOne(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            return optionalStudent.get().mapToStudentDTO();
        } else {
            throw new NotFoundException("Nie znaleziono studenta");
        }
    }

    public StudentAverageCheckResponse checkAvg(Long studentId, Long subjectId) {
        Set<Grade> grades = gradeService.getGrades(studentId, subjectId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        String subjectName = null;
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subjectName = subject.getSubjectName();
        }
        Double avg = calculateAvg(grades);
        boolean gradeAtRisk = avg < 2.0;
        return new StudentAverageCheckResponse(studentId, subjectName, avg, gradeAtRisk);
    }

    private Double calculateAvg(Set<Grade> grades) {
        double sum = 0.0;
        for (Grade grade : grades) {
            sum = sum + grade.getValue();
        }
        return sum / grades.size();
    }
}
