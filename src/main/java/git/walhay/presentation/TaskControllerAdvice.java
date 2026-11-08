package git.walhay.presentation;

import git.walhay.application.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskControllerAdvice {

	@ExceptionHandler(TaskNotFoundException.class)
	public ProblemDetail taskNotFoundException() {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Task not found");
	}
}
