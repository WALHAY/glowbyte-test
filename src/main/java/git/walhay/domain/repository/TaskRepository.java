package git.walhay.domain.repository;

import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.domain.model.Task;
import java.util.List;
import java.util.UUID;

public interface TaskRepository {

	Task create(Task task);

	Task update(Task task);

	void deleteById(UUID id);

	Task findTaskById(UUID id) throws TaskNotFoundException;

	Task findTaskByIdForUpdate(UUID id) throws TaskNotFoundException;

	List<Task> findAllTasks(Boolean completed);
}
