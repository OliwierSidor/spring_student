package arppl4.spring_student.controller;

import arppl4.spring_student.model.dto.AddGradeRequest;
import arppl4.spring_student.model.dto.GradeDTO;
import arppl4.spring_student.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addGrade(@RequestBody AddGradeRequest addGradeRequest) {
        log.info("Wywołano metodę addStudent");
        gradeService.addGrade(addGradeRequest);
    }

    @GetMapping
    public Set<GradeDTO> gradeList(@RequestParam Long studentId) {
        log.info("wywołano metodę gradeList");
        return gradeService.listGrades(studentId);
    }

    @GetMapping("/subjectgrades")
    public Set<GradeDTO> gradeSubjectList(@RequestParam Long subjectId, @RequestParam Long studentId) {
        log.info("wywołano metodę gradeSubjectList");
        return gradeService.listGrades(studentId, subjectId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGrade(@PathVariable(name = "id") Long gradeId) {
        gradeService.deleteGrade(gradeId);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateGrade(@RequestBody GradeDTO gradeDTO) {
        log.info("Wywołano metodę updateGrade");
        gradeService.updateGrade(gradeDTO);
    }

    @GetMapping("/grade")
    public GradeDTO getOneGrade(@RequestParam Long gradeId) {
        log.info("Wywołano metodę oneGrade");
        return gradeService.getOneGrad(gradeId);
    }
}
