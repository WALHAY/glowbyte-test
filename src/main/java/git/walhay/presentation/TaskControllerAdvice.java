package git.walhay.presentation;

import git.walhay.application.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail validationException(MethodArgumentNotValidException exception) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
		problem.setProperty(
				"errors",
				exception.getBindingResult().getFieldErrors().stream()
						.map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
						.toList());
		return problem;
	}

	@ExceptionHandler(TaskNotFoundException.class)
	public ProblemDetail taskNotFoundException() {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Task not found");
	}

	private record ValidationError(String field, String message) {}
}
