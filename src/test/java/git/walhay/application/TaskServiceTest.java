package git.walhay.application;

import git.walhay.application.dto.TaskUpdateCommand;
import git.walhay.application.service.TaskService;
import git.walhay.application.service.TaskServiceImpl;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

		UUID id = UUID.randomUUID();
		Instant createdAt = Instant.now();
		Task task = new Task(id, "Simple task", "Just a simple task", false, createdAt);

		when(repository.findTaskById(id)).thenReturn(task);
		when(repository.update(any(Task.class))).thenAnswer(call -> call.getArgument(0));

		Task result = taskService.updateTask(id, new TaskUpdateCommand("Difficult task", "Task became difficult", true));

		Task expected = new Task(id, "Difficult task", "Task became difficult", true, createdAt);
		assertEquals(expected, result);
		verify(repository).update(expected);
	}
}
