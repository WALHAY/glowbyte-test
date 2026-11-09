package git.walhay.presentation;

import git.walhay.application.exception.TaskNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TaskControllerAdvice extends ResponseEntityExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskControllerAdvice.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		var errors = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
				.toList();
		return handleExceptionInternal(exception, validationProblem(errors), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHandlerMethodValidationException(
			HandlerMethodValidationException exception,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request) {
		if (exception.isForReturnValue()) {
			return super.handleHandlerMethodValidationException(exception, headers, status, request);
		}
		var errors = exception.getParameterValidationResults().stream()
				.flatMap(result -> result.getResolvableErrors().stream()
						.map(error -> new ValidationError(
								result.getMethodParameter().getParameterName(), error.getDefaultMessage())))
				.toList();
		return handleExceptionInternal(exception, validationProblem(errors), headers, status, request);
	}

	@ExceptionHandler(TaskNotFoundException.class)
	public ProblemDetail taskNotFoundException() {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Task not found");
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail unexpectedException(Exception exception) {
		LOGGER.error("Unexpected request failure", exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
	}

	private ProblemDetail validationProblem(List<ValidationError> errors) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request validation failed");
		problem.setProperty("errors", errors);
		return problem;
	}

	private record ValidationError(String field, String message) {}
}
