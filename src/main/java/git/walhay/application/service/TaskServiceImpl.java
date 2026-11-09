package git.walhay.application.service;

import git.walhay.application.dto.Page;
import git.walhay.application.dto.TaskCreateCommand;
import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.dto.TaskUpdateCommand;
import git.walhay.application.model.Task;
import git.walhay.repository.TaskRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Task createTask(TaskCreateCommand command) {
		Task task = new Task(command.name(), command.description(), false);

		return taskRepository.create(task);
	}

	@Override
	public Task updateTask(UUID id, TaskUpdateCommand command) {
		Task task = taskRepository.findTaskByIdForUpdate(id);

		String name = command.name() == null ? task.name() : command.name();
		String description = command.descriptionProvided() ? command.description() : task.description();
		boolean completed = command.completed() == null ? task.completed() : command.completed();

		return taskRepository.update(new Task(task.id(), name, description, completed, task.createdAt()));
	}

	@Override
	public void deleteTask(UUID id) {
		taskRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Task getTask(UUID id) {
		return taskRepository.findTaskById(id);
	}

	@Override
	@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
	public Page<Task> getTasks(TaskListQuery query) {
		return taskRepository.findTasks(query);
	}
}
