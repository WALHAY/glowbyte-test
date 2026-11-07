package git.walhay.domain;

import git.walhay.domain.dto.TaskCreateCommand;
import git.walhay.domain.dto.TaskUpdateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Task createTask(TaskCreateCommand command) {
		return taskRepository.save(command);
	}

	@Override
	public Task updateTask(UUID id, TaskUpdateCommand command) {
		return taskRepository.update(id, command);
	}

	@Override
	public void deleteTask(UUID id) {
		taskRepository.delete(id);
	}

	@Override
	public Task getTask(UUID id) {
		return taskRepository.findTaskById(id);
	}

	@Override
	public List<Task> getTasks(Boolean completed) {
		return taskRepository.findAllTasks(completed);
	}
}
