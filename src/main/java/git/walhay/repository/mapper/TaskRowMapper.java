package git.walhay.repository.mapper;

import git.walhay.application.model.Task;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.jdbc.core.RowMapper;

public class TaskRowMapper implements RowMapper<Task> {

	@Override
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Task(
				rs.getObject("id", UUID.class),
				rs.getString("name"),
				rs.getString("description"),
				rs.getBoolean("completed"),
				rs.getObject("created_at", OffsetDateTime.class).toInstant());
	}
}
