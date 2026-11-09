package git.walhay.application.dto;

import java.util.List;
import java.util.function.Function;

public record Page<T>(List<T> content, int page, int size, long totalElements) {
	public Page {
		if (page < 0) {
			throw new IllegalArgumentException("Page must not be negative");
		}
		if (size < 1) {
			throw new IllegalArgumentException("Size must be positive");
		}
		if (totalElements < 0) {
			throw new IllegalArgumentException("Total elements must not be negative");
		}
		content = List.copyOf(content);
	}

	public int totalPages() {
		return (int) Math.min(Integer.MAX_VALUE, Math.ceilDiv(totalElements, size));
	}

	public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
		List<R> mappedContent = content.stream().<R>map(mapper).toList();
		return new Page<>(mappedContent, page, size, totalElements);
	}
}
