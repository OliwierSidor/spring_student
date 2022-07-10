package arppl4.spring_student.exception;

public class StudentHaveGradesException extends RuntimeException{
    public StudentHaveGradesException(String message){
        super(message);
    }
}
