package git.walhay.application.dto;

import java.util.List;
import java.util.function.Function;

public record Page<T>(List<T> content, int page, int size, long totalElements) {
	public Page {
		content = List.copyOf(content);
	}

	public int totalPages() {
		return (int) Math.ceil((double) totalElements / size);
	}

	public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
		List<R> mappedContent = content.stream().<R>map(mapper).toList();
		return new Page<>(mappedContent, page, size, totalElements);
	}
}
