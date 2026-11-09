package git.walhay.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import git.walhay.application.dto.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

public class PageTest {

	@Test
	public void testMappingPreservesPagination() {
		Page<Integer> page = new Page<>(List.of(5), 2, 2, 5);
		Function<Number, String> mapper = Number::toString;

		Page<CharSequence> mapped = page.map(mapper);

		assertEquals(List.of("5"), mapped.content());
		assertEquals(2, mapped.page());
		assertEquals(2, mapped.size());
		assertEquals(5, mapped.totalElements());
		assertEquals(3, mapped.totalPages());
	}

	@Test
	public void testContentIsAnImmutableSnapshot() {
		List<Integer> content = new ArrayList<>(List.of(1, 2));
		Page<Integer> page = new Page<>(content, 0, 2, 2);

		content.clear();
		Page<String> mapped = page.map(Object::toString);

		assertEquals(List.of(1, 2), page.content());
		assertEquals(List.of("1", "2"), mapped.content());
		assertThrows(UnsupportedOperationException.class, () -> page.content().clear());
		assertThrows(UnsupportedOperationException.class, () -> mapped.content().clear());
	}
}
