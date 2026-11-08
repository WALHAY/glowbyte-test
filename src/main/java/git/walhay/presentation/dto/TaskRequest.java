package git.walhay.presentation.dto;

import git.walhay.domain.model.Task;
import java.time.Instant;
import java.util.UUID;

public record TaskRequest(UUID id, String name, String description, boolean completed, Instant createdAt) {

	public static TaskRequest fromTask(Task task) {
		return new TaskRequest(task.id(), task.name(), task.description(), task.completed(), task.createdAt());
	}
}
