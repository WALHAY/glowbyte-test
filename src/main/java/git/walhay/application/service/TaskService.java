package git.walhay.application.service;

import git.walhay.domain.model.Task;
import git.walhay.application.dto.TaskCreateCommand;
import git.walhay.application.dto.TaskUpdateCommand;

import java.util.List;
import java.util.UUID;

public interface TaskService {

	Task createTask(TaskCreateCommand command);

	Task updateTask(UUID id, TaskUpdateCommand command);
			
	void deleteTask(UUID id);

	Task getTask(UUID id);

	List<Task> getTasks(Boolean completed);
}
