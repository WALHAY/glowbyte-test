package git.walhay.repository;

import git.walhay.application.dto.Page;
import git.walhay.application.dto.TaskListQuery;
import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.application.model.Task;
import git.walhay.repository.mapper.TaskRowMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTaskRepositoryImpl implements TaskRepository {

	private final JdbcTemplate jdbcTemplate;
	private final TaskRowMapper taskRowMapper = new TaskRowMapper();

	public JdbcTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Task create(Task task) {
		final String sql = """
			insert into tasks (id, name, description, completed, created_at)
			values (?, ?, ?, ?, coalesce(?, CURRENT_TIMESTAMP))
			""";

		jdbcTemplate.update(sql, task.id(), task.name(), task.description(), task.completed(), task.createdAt());

		return jdbcTemplate.queryForObject("SELECT * FROM tasks WHERE id = ?", taskRowMapper, task.id());
	}

	@Override
	public Task update(Task task) {
		final String sql = """
			update tasks
			set name = ?, description = ?, completed = ?
			where id = ?
			""";

		int updated = jdbcTemplate.update(sql, task.name(), task.description(), task.completed(), task.id());
		if (updated == 0) {
			throw new TaskNotFoundException(task.id());
		}

		return findTaskById(task.id());
	}

	@Override
	public void deleteById(UUID id) {
		final String deleteQuery = """
			delete from tasks
			where id = ?
			""";

		jdbcTemplate.update(deleteQuery, id);
	}

	@Override
	public Task findTaskById(UUID id) throws TaskNotFoundException {
		return findTaskById(id, false);
	}

	@Override
	public Task findTaskByIdForUpdate(UUID id) throws TaskNotFoundException {
		return findTaskById(id, true);
	}

	private Task findTaskById(UUID id, boolean forUpdate) {
		final String findByIdQuery = """
			select *
			from tasks
			where id = ?
			""";

		try {
			return jdbcTemplate.queryForObject(findByIdQuery + (forUpdate ? " for update" : ""), taskRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			throw new TaskNotFoundException(id);
		}
	}

	@Override
	public Page<Task> findTasks(TaskListQuery query) {
		final String countQuery = """
			select count(*)
			from tasks
			where ? is null or completed = ?
			""";
		final String findByCompletionQuery = """
			select *
			from tasks
			where ? is null or completed = ?
			order by created_at desc, id desc
			limit ? offset ?
			""";

		long totalElements = jdbcTemplate.queryForObject(countQuery, Long.class, query.completed(), query.completed());
		List<Task> content = jdbcTemplate.query(
				findByCompletionQuery,
				taskRowMapper,
				query.completed(),
				query.completed(),
				query.size(),
				query.offset());
		return new Page<>(content, query.page(), query.size(), totalElements);
	}
}
