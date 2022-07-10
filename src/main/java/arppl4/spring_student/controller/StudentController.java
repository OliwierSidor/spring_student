package arppl4.spring_student.controller;

import arppl4.spring_student.model.Student;
import arppl4.spring_student.model.dto.AddStudentRequest;
import arppl4.spring_student.model.dto.StudentAverageCheckRequest;
import arppl4.spring_student.model.dto.StudentAverageCheckResponse;
import arppl4.spring_student.model.dto.StudentDTO;
import arppl4.spring_student.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentDTO> findAll(){
        log.info("Wywołano metodę findAll");
        return studentService.findAllStudents();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addStudent(@RequestBody AddStudentRequest student){
        log.info("Wywołano metodę addStudent");
        studentService.addStudent(student);
    }
    @PostMapping("/addstudents")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStudents(@RequestBody List<AddStudentRequest> list){
        log.info("Wywołano metodę addStudents");
        studentService.addStudents(list);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateStudent(@RequestBody StudentDTO studentDTO){
        log.info("Wywołano metodę updateStudent");
        studentService.updateStudent(studentDTO);
    }
    @DeleteMapping("/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable(name = "identifier") Long studentId){
        log.info("Wywołano metodę deleteStudent");
        studentService.deleteStudent(studentId);
    }
    @GetMapping("/findone")
    public StudentDTO findOne(@RequestParam Long studentId){
        log.info("wywołano metodę findOne");
        return studentService.findOne(studentId);
    }

    @GetMapping("/avg")
    public StudentAverageCheckResponse checkAvg(@RequestBody StudentAverageCheckRequest request){
        log.info("Wywołano metodę checkAvg");
        return  studentService.checkAvg(request.getStudentId());
    }
}
