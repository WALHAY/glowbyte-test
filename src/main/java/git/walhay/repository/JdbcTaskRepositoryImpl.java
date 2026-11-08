package git.walhay.repository;

import git.walhay.application.exception.TaskNotFoundException;
import git.walhay.domain.model.Task;
import git.walhay.domain.repository.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcTaskRepositoryImpl implements TaskRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Task save(Task task) {
		final String insertQuery = """
			insert into tasks (id, name, description, completed)
			values (?, ?, ?, ?)
			""";

		return jdbcTemplate.queryForObject(
			insertQuery,
			(rs, rowNum) -> new Task(
				(UUID) rs.getObject("id"),
				rs.getString("name"),
				rs.getString("description"),
				rs.getBoolean("completed"),
				rs.getDate("createdAt").toInstant()
			),
			task.id(),
			task.name(),
			task.description(),
			task.completed()
		);
	}

    @Override
    public Task update(Task task) {
        return null;
    }

	@Override
	public void deleteById(UUID id) {
		
	}

	@Override
	public Task findTaskById(UUID id) throws TaskNotFoundException {
		return null;
	}

	@Override
	public List<Task> findAllTasks(Boolean completed) {
		return List.of();
	}
}
