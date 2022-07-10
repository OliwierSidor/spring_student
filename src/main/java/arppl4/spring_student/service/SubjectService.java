package arppl4.spring_student.service;

import arppl4.spring_student.exception.NotFoundException;
import arppl4.spring_student.exception.SubjectAlreadyExistsException;
import arppl4.spring_student.model.Grade;
import arppl4.spring_student.model.Subject;
import arppl4.spring_student.model.dto.AddSubjectRequest;
import arppl4.spring_student.model.dto.StudentSubjectAverageResponse;
import arppl4.spring_student.model.dto.SubjectDTO;
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
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final GradeService gradeService;


    public void addSubject(AddSubjectRequest addSubjectRequest) {
        List<Subject> subjects = subjectRepository.findAll();

        for (Subject subject : subjects) {
            String subjectNoSpaces = subject.getSubjectName().replace(" ", "");
            String receivedSubjectNoSpaces = addSubjectRequest.getSubjectName().replace(" ", "");
            if (subjectNoSpaces.equalsIgnoreCase(receivedSubjectNoSpaces)) {
                log.info("Nie mogę dodać przedmiotu bo już taki istnieje");
                throw new SubjectAlreadyExistsException("Nie mogę dodać przedmiotu bo już taki istnieje");
            }
        }
        addSubjectRequest.setSubjectName(addSubjectRequest.getSubjectName().replace(" ", "").toLowerCase());
        Subject newSubject = new Subject(addSubjectRequest);
        subjectRepository.save(newSubject);
    }

    public List<SubjectDTO> listAllStudents() {
        List<Subject> subjectList = subjectRepository.findAll();
        List<SubjectDTO> subjectDTOS = new ArrayList<>();
        for (Subject subject : subjectList) {
            subjectDTOS.add(subject.mapToSubjectDTO());
        }
        return subjectDTOS;
    }

    public void deleteSubject(Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            if (subject.getGradeList().isEmpty()) {
                subjectRepository.delete(subject);
                log.info("Usunięto przedmiot: " + subject);
            } else {
                log.info("Nie można usunąć " + subject + " bo ma oceny");
            }
        } else {
            throw new EntityNotFoundException("Nie znaleziono przedmiotu o id: " + subjectId);
        }
    }

    public void addSubjects(List<AddSubjectRequest> list) {
        for (AddSubjectRequest addSubjectRequest : list) {
            addSubject(addSubjectRequest);
        }
    }

    public SubjectDTO getSubject(Long subjectId) {
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        if (optionalSubject.isPresent()) {
            return optionalSubject.get().mapToSubjectDTO();
        }
        throw new NotFoundException("Nie znaleziono przedmiotu");
    }

    public StudentSubjectAverageResponse subjectAvg(Long studentId, Long subjectId) {
        Set<Grade> grades = gradeService.getGrades(studentId, subjectId);
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        String subjectName = null;
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subjectName = subject.getSubjectName();
        }
        return new StudentSubjectAverageResponse(studentId, subjectName, calculateAvg(grades));
    }

    private Double calculateAvg(Set<Grade> grades) {
        double sum = 0.0;
        for (Grade grade : grades) {
            sum = sum + grade.getValue();
        }
        return sum / grades.size();
    }
}
