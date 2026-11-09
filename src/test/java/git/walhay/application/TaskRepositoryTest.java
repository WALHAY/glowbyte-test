package git.walhay.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.application.port.out.TaskRepository;
import git.walhay.domain.model.Task;
import java.time.Instant;
import java.util.List;
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

		var all = taskRepository.findTasks(new TaskListQuery(null, 0, 20));
		var completed = taskRepository.findTasks(new TaskListQuery(true, 0, 20));
		var notCompleted = taskRepository.findTasks(new TaskListQuery(false, 0, 20));

		assertEquals(3, all.content().size());
		assertEquals(3, all.totalElements());
		assertEquals(2, completed.content().size());
		assertEquals(2, completed.totalElements());
		assertEquals(1, notCompleted.content().size());
		assertEquals(1, notCompleted.totalElements());
	}

	@Test
	public void testPaginationWithEqualCreationTimes() {
		Instant createdAt = Instant.parse("2024-01-01T12:00:00Z");
		Task task1 = new Task(new UUID(0, 1), "First task", null, false, createdAt);
		Task task2 = new Task(new UUID(0, 2), "Second task", null, true, createdAt);
		Task task3 = new Task(new UUID(0, 3), "Third task", null, true, createdAt);
		Task olderTask = new Task(new UUID(0, 4), "Older task", null, false, createdAt.minusSeconds(1));
		List.of(task1, task2, task3, olderTask).forEach(taskRepository::create);

		var first = taskRepository.findTasks(new TaskListQuery(null, 0, 2));
		var second = taskRepository.findTasks(new TaskListQuery(null, 1, 2));
		var beyondLast = taskRepository.findTasks(new TaskListQuery(null, 2, 2));
		var completed = taskRepository.findTasks(new TaskListQuery(true, 1, 1));

		assertEquals(List.of(task3, task2), first.content());
		assertEquals(0, first.page());
		assertEquals(2, first.size());
		assertEquals(4, first.totalElements());
		assertEquals(2, first.totalPages());
		assertEquals(List.of(task1, olderTask), second.content());
		assertEquals(1, second.page());
		assertEquals(4, second.totalElements());
		assertEquals(List.of(), beyondLast.content());
		assertEquals(2, beyondLast.page());
		assertEquals(4, beyondLast.totalElements());
		assertEquals(2, beyondLast.totalPages());
		assertEquals(List.of(task2), completed.content());
		assertEquals(2, completed.totalElements());
		assertEquals(2, completed.totalPages());
	}

	@Test
	public void testEmptyPage() {
		var page = taskRepository.findTasks(new TaskListQuery(null, 0, 20));

		assertEquals(List.of(), page.content());
		assertEquals(0, page.totalElements());
		assertEquals(0, page.totalPages());
	}
}
