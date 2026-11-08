package git.walhay.presentation;

import git.walhay.application.service.TaskService;
import git.walhay.presentation.dto.TaskCreateDto;
import git.walhay.presentation.dto.TaskRequest;
import git.walhay.presentation.dto.TaskUpdateDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
	public List<TaskRequest> getAllTasks(@RequestParam(required = false) Boolean completed) {
		return taskService.getTasks(completed).stream()
				.map(TaskRequest::fromTask)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public TaskRequest getTaskById(@PathVariable UUID id) {
		return TaskRequest.fromTask(taskService.getTask(id));
	}

	@PostMapping
	public TaskRequest createTask(@RequestBody @Valid TaskCreateDto createDto) {
		return TaskRequest.fromTask(taskService.createTask(createDto.toCommand()));
	}

	@PatchMapping("/{id}")
	public TaskRequest updateTask(@PathVariable UUID id, @RequestBody @Valid TaskUpdateDto updateDto) {
		return TaskRequest.fromTask(taskService.updateTask(id, updateDto.toCommand()));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTask(@PathVariable UUID id) {
		taskService.deleteTask(id);
	}
}
