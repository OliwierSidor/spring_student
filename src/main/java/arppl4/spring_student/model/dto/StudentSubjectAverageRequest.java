package arppl4.spring_student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubjectAverageRequest {
    private Long studentId;
    private Long subjectId;
}
