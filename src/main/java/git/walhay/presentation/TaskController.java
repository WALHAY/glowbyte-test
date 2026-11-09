package git.walhay.presentation;

import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.service.TaskService;
import git.walhay.presentation.dto.PageResponse;
import git.walhay.presentation.dto.TaskCreateDto;
import git.walhay.presentation.dto.TaskResponse;
import git.walhay.presentation.dto.TaskUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskCreateDto createDto) {
		var task = taskService.createTask(createDto.toCommand());
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(task.id())
				.toUri();
		return ResponseEntity.created(location).body(TaskResponse.fromTask(task));
	}

	@PatchMapping("/{id}")
	public TaskResponse updateTask(@PathVariable UUID id, @RequestBody @Valid TaskUpdateDto updateDto) {
		return TaskResponse.fromTask(taskService.updateTask(id, updateDto.toCommand()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTask(@PathVariable UUID id) {
		taskService.deleteTask(id);
	}
}
