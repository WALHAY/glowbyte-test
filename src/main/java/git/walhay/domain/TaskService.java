package git.walhay.domain;

import git.walhay.domain.dto.TaskCreateCommand;
import git.walhay.domain.dto.TaskUpdateCommand;

import java.util.List;
import java.util.UUID;

interface TaskService {

	Task createTask(TaskCreateCommand command);

	Task updateTask(UUID id, TaskUpdateCommand command);
			
	void deleteTask(UUID id);

	Task getTask(UUID id);

	List<Task> getTasks(Boolean completed);
}
