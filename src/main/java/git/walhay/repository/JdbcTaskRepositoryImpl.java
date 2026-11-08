package git.walhay.repository;

import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import git.walhay.repository.mapper.TaskRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcTaskRepositoryImpl implements TaskRepository {

	private final JdbcTemplate jdbcTemplate;
	private final TaskRowMapper taskRowMapper = new TaskRowMapper();

	public JdbcTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Task save(Task task) {
		final String sql = """
			merge into tasks (id, name, description, completed, created_at)
			key (id)
			values (?, ?, ?, ?, coalesce(?, CURRENT_TIMESTAMP))
			""";

		jdbcTemplate.update(
			sql,
			task.id(),
			task.name(),
			task.description(),
			task.completed(),
			task.createdAt()
		);

		return jdbcTemplate.queryForObject(
			"SELECT * FROM tasks WHERE id = ?",
			taskRowMapper,
			task.id()
		);
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
		final String findByIdQuery = """
			select *
			from tasks
			where id = ?
			""";

		try {
			return jdbcTemplate.queryForObject(findByIdQuery, taskRowMapper, id);
		} catch(EmptyResultDataAccessException e) {
			throw new TaskNotFoundException(id);
		}
	}

	@Override
	public List<Task> findAllTasks(Boolean completed) {
		final String findByCompletionQuery = """
			select *
			from tasks
			where ? is null or completed = ?
			""";

		return jdbcTemplate.query(findByCompletionQuery, taskRowMapper, completed, completed);
	}
}
