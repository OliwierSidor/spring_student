package arppl4.spring_student.controller;

import arppl4.spring_student.model.dto.AddSubjectRequest;
import arppl4.spring_student.model.dto.StudentSubjectAverageRequest;
import arppl4.spring_student.model.dto.StudentSubjectAverageResponse;
import arppl4.spring_student.model.dto.SubjectDTO;
import arppl4.spring_student.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addSubject(@RequestBody AddSubjectRequest addSubjectRequest) {
        log.info("Wywołano metodę addSubject " + addSubjectRequest);
        subjectService.addSubject(addSubjectRequest);
    }

    @PostMapping("/addsubjects")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSubjects(@RequestBody List<AddSubjectRequest> list) {
        log.info("Wywołano metodę addStudents");
        subjectService.addSubjects(list);
    }

    @GetMapping()
    public List<SubjectDTO> listAllSubjects() {
        log.info("Wywołano metodę listAllSubjects");
        return subjectService.listAllStudents();
    }

    @DeleteMapping("/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable(name = "identifier") Long subjectId) {
        subjectService.deleteSubject(subjectId);
    }

    @GetMapping("/subject")
    public SubjectDTO Subject(@RequestParam Long subjectId) {
        log.info("Wywołano metodę Subject");
        return subjectService.getSubject(subjectId);
    }
    @GetMapping("/avg")
    public StudentSubjectAverageResponse subjectAvg(@RequestBody StudentSubjectAverageRequest request){
        log.info("Wywołano metodę subjectAvg");
        return subjectService.subjectAvg(request.getStudentId(), request.getSubjectId());
    }
}
