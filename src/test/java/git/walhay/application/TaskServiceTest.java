package git.walhay.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import git.walhay.application.dto.TaskUpdateCommand;
import git.walhay.application.service.TaskService;
import git.walhay.application.service.TaskServiceImpl;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/*
 * Test are redundant for this project as its logic is very simple
 * so we dont actually want to mock return value and compare two equal value
 * as they are always equal.
 *
 * Single test just to be sure
 */
public class TaskServiceTest {

	@Test
	public void taskUpdateTest() {
		TaskRepository repository = Mockito.mock(TaskRepository.class);
		TaskService taskService = new TaskServiceImpl(repository);

		Task task = ObjectMother.task();

		when(repository.findTaskById(task.id())).thenReturn(task);
		when(repository.save(any(Task.class))).thenAnswer(call -> call.getArgument(0));

		Task result = taskService.updateTask(
				task.id(), new TaskUpdateCommand("Difficult task", "Task became difficult", true));

		Task expected = new Task(task.id(), "Difficult task", "Task became difficult", true, task.createdAt());
		assertEquals(expected, result);
		verify(repository).save(expected);
	}
}
