package git.walhay.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Task(UUID id, String name, String description, boolean completed, Instant createdAt) {
	public Task(String name, String description, boolean completed) {
		this(UUID.randomUUID(), name, description, completed, Instant.now());
	}
}
