package git.walhay.presentation.dto;

import git.walhay.domain.model.Task;
import java.time.Instant;
import java.util.UUID;

public record TaskResponse(UUID id, String name, String description, boolean completed, Instant createdAt) {

	public static TaskResponse fromTask(Task task) {
		return new TaskResponse(task.id(), task.name(), task.description(), task.completed(), task.createdAt());
	}
}
