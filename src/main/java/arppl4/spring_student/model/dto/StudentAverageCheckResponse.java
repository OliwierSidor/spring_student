package arppl4.spring_student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAverageCheckResponse {
    private Long studentId;
    private String subjectName;
    private Double average;
    private boolean gradeAtRisk;
}
