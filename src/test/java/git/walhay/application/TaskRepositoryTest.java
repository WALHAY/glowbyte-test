package git.walhay.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TaskRepositoryTest {

	private final TaskRepository taskRepository;

	@Autowired
	public TaskRepositoryTest(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Test
	public void testTaskInsertion() {
		Task task = ObjectMother.task();

		Task result = taskRepository.create(task);

		assertEquals(task, result);
		assertEquals(task, taskRepository.findTaskById(task.id()));
	}

	@Test
	public void testTaskUpdate() {
		Task task = ObjectMother.task();

		taskRepository.create(task);

		Task redacted = new Task(task.id(), "Edited task", "Edited description", true, task.createdAt());

		Task result = taskRepository.update(redacted);

		assertEquals(redacted, result);
		assertEquals(redacted, taskRepository.findTaskById(task.id()));
	}

	@Test
	public void testTaskDeletion() {
		Task task = ObjectMother.task();
		Task otherTask = ObjectMother.task();

		taskRepository.create(task);
		taskRepository.create(otherTask);
		taskRepository.deleteById(task.id());

		assertThrows(TaskNotFoundException.class, () -> taskRepository.findTaskById(task.id()));
		assertEquals(otherTask, taskRepository.findTaskById(otherTask.id()));
	}

	@Test
	public void testMissingTaskLookup() {
		UUID missingId = UUID.randomUUID();

		assertThrows(TaskNotFoundException.class, () -> taskRepository.findTaskById(missingId));
	}

	@Test
	public void testMissingTaskDeletion() {
		Task task = ObjectMother.task();

		taskRepository.create(task);

		UUID missingId = UUID.randomUUID();

		assertDoesNotThrow(() -> taskRepository.deleteById(missingId));
		assertEquals(task, taskRepository.findTaskById(task.id()));
	}

	@Test
	public void testTaskInsertionWithoutDescription() {
		Task task = ObjectMother.task();
		Task taskWithoutDescription = new Task(task.id(), task.name(), null, task.completed(), task.createdAt());

		Task result = taskRepository.create(taskWithoutDescription);

		assertEquals(taskWithoutDescription, result);
		assertEquals(taskWithoutDescription, taskRepository.findTaskById(task.id()));
	}

	@Test
	public void testTaskInsertionWithoutCreationTime() {
		Task task = ObjectMother.task();
		Task taskWithoutCreationTime = new Task(task.id(), task.name(), task.description(), task.completed(), null);

		Task result = taskRepository.create(taskWithoutCreationTime);

		assertNotNull(result.createdAt());
		assertEquals(task.id(), result.id());
		assertEquals(task.name(), result.name());
		assertEquals(task.description(), result.description());
		assertEquals(task.completed(), result.completed());
		assertEquals(result, taskRepository.findTaskById(task.id()));
	}

	@Test
	public void testFindAllTasks() {
		Task task = ObjectMother.task();
		Task task2 = new Task(UUID.randomUUID(), "Completed task", "Do task", true, null);
		Task task3 = new Task(UUID.randomUUID(), "Another successful", "Success", true, null);

		taskRepository.create(task);
		taskRepository.create(task2);
		taskRepository.create(task3);

		var all = taskRepository.findAllTasks(null);
		var completed = taskRepository.findAllTasks(true);
		var notCompleted = taskRepository.findAllTasks(false);

		System.out.println(all.size());
		assertEquals(3, all.size());
		assertEquals(2, completed.size());
		assertEquals(1, notCompleted.size());
	}
}
