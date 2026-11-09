package git.walhay.presentation.dto;

import git.walhay.application.dto.Page;
import java.util.List;

public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
	public static <T> PageResponse<T> fromPage(Page<T> page) {
		return new PageResponse<>(page.content(), page.page(), page.size(), page.totalElements(), page.totalPages());
	}
}
