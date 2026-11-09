package git.walhay.application.dto;

public record TaskListQuery(Boolean completed, int page, int size) {
	public static final int DEFAULT_PAGE = 0;
	public static final int DEFAULT_SIZE = 20;
	public static final int MAX_SIZE = 100;

	public TaskListQuery {
		if (page < 0) {
			throw new IllegalArgumentException("Page must not be negative");
		}
		if (size < 1 || size > MAX_SIZE) {
			throw new IllegalArgumentException("Size must be between 1 and " + MAX_SIZE);
		}
	}

	public long offset() {
		return (long) page * size;
	}
}
