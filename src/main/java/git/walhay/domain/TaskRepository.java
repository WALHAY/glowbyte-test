package git.walhay.domain;

import git.walhay.domain.dto.TaskCreateCommand;
import git.walhay.domain.dto.TaskUpdateCommand;

import java.util.List;
import java.util.UUID;

public interface TaskRepository {

	Task save(TaskCreateCommand command);

	void delete(UUID id);

	Task update(UUID id, TaskUpdateCommand command);

	Task findTaskById(UUID id);

	List<Task> findAllTasks(Boolean completed);
}
