package arppl4.spring_student.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddGradeRequest {
    private Long studentId;
    private Long subjectId;
    private Integer value;
}
