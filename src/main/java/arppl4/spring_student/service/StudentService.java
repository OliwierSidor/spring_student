package arppl4.spring_student.service;

import arppl4.spring_student.exception.NotFoundException;
import arppl4.spring_student.exception.StudentHaveGradesException;
import arppl4.spring_student.exception.ValueNotSetException;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public List<StudentDTO> findAllStudents() {
        List<Student> studentList = studentRepository.findAll();
        return studentList.stream().map(Student::mapToStudentDTO).collect(Collectors.toList());
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
            if (studentDTO.getName() == null && studentDTO.getSurname() == null && studentDTO.getDateOfBirth() == null) {
                throw new ValueNotSetException("Nie podałeś żadnego pola do edycji");
            }
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
        } else {
            throw new EntityNotFoundException("Nie znaleziono studenta o id: " + id);
        }
    }

    public void deleteStudent(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getGradeList().isEmpty()) {
                studentRepository.delete(student);
                log.info("Usunięto studenta: " + student);
            } else {
                throw new StudentHaveGradesException("Nie można usunąć studenta który ma oceny");
            }
        } else {
            throw new EntityNotFoundException("Nie znaleziono studenta o id: " + id);
        }
    }

    public void addStudents(List<AddStudentRequest> list) {
        list.stream().forEach(this::addStudent);
    }

    public StudentDTO findOne(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            return optionalStudent.get().mapToStudentDTO();
        } else {
            throw new NotFoundException("Nie znaleziono studenta");
        }
    }

    public Set<StudentAverageCheckResponse> checkAvg(Long studentId) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            return optionalStudent.get().getGradeList().stream() // wez wszystkie oceny studenta
                    .collect(Collectors.groupingBy(grade -> grade.getSubject().getSubjectName())).entrySet().stream() // pogrupuj je po przedmiocie
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> calculateAvg(e.getValue()))).entrySet().stream() // policz srednia dla kazdego przedmiotu
                    .map(e -> new StudentAverageCheckResponse(studentId, e.getKey(), e.getValue(), e.getValue() < 2.0)) // stworz wynik dla kazdego przedmiotu
                    .collect(Collectors.toSet()); // zbierz wyniki do zbioru
        } else {
            throw new NotFoundException("Nie znaleziono przedmiotu lub studenta");
        }
    }

    private Double calculateAvg(Collection<Grade> grades) {
        double sum = 0.0;
        if (!grades.isEmpty()) {
            for (Grade grade : grades) {
                sum = sum + grade.getValue();
            }
            return sum / grades.size();
        } else {
            throw new NotFoundException("Lista ocen jest pusta - nie można dzielić przez zero");
        }
    }
}
