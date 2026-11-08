package git.walhay.application;

import git.walhay.domain.model.Task;
import java.time.Instant;
import java.util.UUID;

public class ObjectMother {
	private static final Instant CREATED_AT = Instant.parse("2024-01-01T12:00:00Z");

	public static Task task() {
		return new Task(UUID.randomUUID(), "Simple task", "Just a simple task", false, CREATED_AT);
	}
}
