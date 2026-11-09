package git.walhay.presentation.config;

import git.walhay.application.dto.TaskListQuery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
@EnableSpringDataWebSupport
public class PaginationConfiguration {
	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
		return resolver -> {
			resolver.setMaxPageSize(TaskListQuery.MAX_SIZE);
			resolver.setFallbackPageable(PageRequest.of(TaskListQuery.DEFAULT_PAGE, TaskListQuery.DEFAULT_SIZE));
		};
	}
}
