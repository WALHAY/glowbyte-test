package git.walhay.presentation;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.service.TaskService;
import git.walhay.presentation.dto.PageResponse;
import git.walhay.presentation.dto.TaskCreateDto;
import git.walhay.presentation.dto.TaskResponse;
import git.walhay.presentation.dto.TaskUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	private final TaskService taskService;

	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public PageResponse<TaskResponse> getAllTasks(
			@RequestParam(required = false) Boolean completed,
			@RequestParam(defaultValue = "" + TaskListQuery.DEFAULT_PAGE) @Min(0) int page,
			@RequestParam(defaultValue = "" + TaskListQuery.DEFAULT_SIZE) @Min(1) @Max(TaskListQuery.MAX_SIZE) int size) {
		var tasks = taskService.getTasks(new TaskListQuery(completed, page, size));
		return PageResponse.fromPage(tasks.map(TaskResponse::fromTask));
	}

	@GetMapping("/{id}")
	public TaskResponse getTaskById(@PathVariable UUID id) {
		return TaskResponse.fromTask(taskService.getTask(id));
	}

	@PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public TaskResponse createTask(@ModelAttribute @Valid TaskCreateDto createDto) {
		return TaskResponse.fromTask(taskService.createTask(createDto.toCommand()));
	}

	@PatchMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
	public TaskResponse updateTask(@PathVariable UUID id, @ModelAttribute @Valid TaskUpdateDto updateDto) {
		return TaskResponse.fromTask(taskService.updateTask(id, updateDto.toCommand()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTask(@PathVariable UUID id) {
		taskService.deleteTask(id);
	}
}
