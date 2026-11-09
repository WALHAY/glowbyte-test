package git.walhay.application.service;

import git.walhay.application.dto.Page;
import git.walhay.application.dto.TaskCreateCommand;
import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.dto.TaskUpdateCommand;
import git.walhay.domain.model.Task;
import java.util.UUID;

public interface TaskService {

	Task createTask(TaskCreateCommand command);

	Task updateTask(UUID id, TaskUpdateCommand command);

	void deleteTask(UUID id);

	Task getTask(UUID id);

	Page<Task> getTasks(TaskListQuery query);
}
