package git.walhay.application.service;

import git.walhay.application.dto.TaskCreateCommand;
import git.walhay.application.dto.TaskUpdateCommand;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Task createTask(TaskCreateCommand command) {
		Task task = new Task(null, command.name(), command.description(), false, null);

		return taskRepository.save(task);
	}

	@Override
	public Task updateTask(UUID id, TaskUpdateCommand command) {
		Task task = taskRepository.findTaskById(id);

		String name = command.name() == null ? task.name() : command.name();
		String description = command.description() == null ? task.description() : command.description();
		boolean completed = command.completed() == null ? task.completed() : command.completed();

		return taskRepository.save(new Task(task.id(), name, description, completed, task.createdAt()));
	}

	@Override
	public void deleteTask(UUID id) {
		taskRepository.deleteById(id);
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
