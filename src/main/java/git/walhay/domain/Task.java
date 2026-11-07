package git.walhay.domain;

import java.util.Date;
import java.util.UUID;

public record Task(
	UUID id,
	String name,
	String description,
	boolean completed,
	Date createdAt
) {}
