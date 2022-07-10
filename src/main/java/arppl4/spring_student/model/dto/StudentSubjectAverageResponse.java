package arppl4.spring_student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSubjectAverageResponse {
    private Long studentId;
    private String subjectName;
    private Double average;
}
