package git.walhay.application.port.out;

import git.walhay.application.dto.Page;
import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.domain.model.Task;
import java.util.UUID;

public interface TaskRepository {

	Task create(Task task);

	Task update(Task task);

	void deleteById(UUID id);

	Task findTaskById(UUID id) throws TaskNotFoundException;

	Task findTaskByIdForUpdate(UUID id) throws TaskNotFoundException;

	Page<Task> findTasks(TaskListQuery query);
}
